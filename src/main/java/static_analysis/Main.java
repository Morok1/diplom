package static_analysis;

public class Main {
    private int seniority;
    private int monthsDisabled;
    private boolean isPartTime;

    double disabilityAmount() {
        if (seniority < 2) {
            return 0;
        }
        if (monthsDisabled > 12) {
            return 0;
        }
        if (isPartTime) {
            return 0;
        }
        // Compute the disability amount.
        // ...
        return 0;
    }
}
