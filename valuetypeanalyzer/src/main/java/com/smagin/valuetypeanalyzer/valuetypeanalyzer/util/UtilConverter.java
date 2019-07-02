package com.smagin.valuetypeanalyzer.valuetypeanalyzer.util;

import com.smagin.valuetypeanalyzer.valuetypeanalyzer.model.Report;

import java.util.*;
import java.util.function.Predicate;

public final class UtilConverter {

    /**
     * Convert to map
     */
    public static Map<String, Report> convertToMapString(Map<String, Map<String, Report>> map) {
        Map<String, Report> bugReportMap = new HashMap<>();

        Set<Map.Entry<String, Map<String, Report>>> entrySet = map.entrySet();
        for (Map.Entry<String, Map<String, Report>> mapEntry : entrySet) {
            Report bugReport = new Report();
            bugReport.setClassName(mapEntry.getKey());
            createGeneralReport(mapEntry.getValue(), bugReport);
            bugReportMap.put(mapEntry.getKey(), bugReport);
        }
        return bugReportMap;
    }

    /**
     * @param map
     * This function
     */
    private static void createGeneralReport(Map<String, Report> map, Report bugReport) {
        StringBuilder stringBuilder  = new StringBuilder();
        boolean generalResult = map
                .values()
                .stream()
                .allMatch(isTrueVerdict());

        map.values().stream()
                .filter(s -> Objects.nonNull(s.getReason()))
                .map(Report::getReason)
                .forEach(s -> stringBuilder.append(s).append(" "));

        String reason = stringBuilder.toString();

        Optional.ofNullable(bugReport).ifPresent(s -> s.setResult(generalResult));
        Optional.ofNullable(bugReport).ifPresent(s -> s.setReason(reason));
    }

    private static Predicate<Report> isTrueVerdict() {
        return s -> s.isResult() == Boolean.TRUE;
    }

}
