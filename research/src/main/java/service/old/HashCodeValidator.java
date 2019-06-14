package service.old;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.stereotype.Service;
import to_heroku.model.BugReport;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Service
@Data
@AllArgsConstructor
public class HashCodeValidator implements Validator {
    private Validator root;
    private static final String result = "Validated!";

    @Override
    public void validate(Class<?> clazz, Map<String, List<BugReport>> bugReports) {

        String className = clazz.getCanonicalName();

        BugReport bugReport = new BugReport();

        bugReport.setClassName(className);
        bugReport.setVerdict(result);

        List<BugReport> reports = bugReports.getOrDefault(className, new ArrayList<>());
        reports.add(bugReport);

        bugReports.put(className, reports);

        if (Objects.nonNull(root)) {
            root.validate(clazz, bugReports);
        }
    }
}
