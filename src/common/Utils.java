package common;

public class Utils {
    public boolean isValidDate(String[] date) {
        try {
            int day = Integer.parseInt(date[0]);
            int month = Integer.parseInt(date[1]);
            int year = Integer.parseInt(date[2]);

            if (day < 1 || day > 31) {
                return false;
            }
            if (month < 1 || month > 12) {
                return false;
            }
            // Allow years from 2020 up to 10 years from now
            int currentYear = java.util.Calendar.getInstance().get(java.util.Calendar.YEAR);
            if (year < 2020 || year > currentYear + 10) {
                return false;
            }
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public Integer convertStringToNumber(String date) {
        return Integer.parseInt(date);
    }
}
