package pl.m4code.utils;

public class  TimeUtil {

    public static String longToString(long value) {
        long a = value / 100L;
        long b = value % 100L;

        if(b == 0) return String.format("%d", a);
        else return String.format("%d.%02d", a, b);
    }

    public static String formatTime(long date) {
        int sec = (int)(date / 20L);
        int min = 0;
        int hours = 0;
        int days = 0;
        if (sec >= 60) {
            min = Math.round(((float) sec / 60));
        }
        if (min >= 60) {
            int h = Math.round(((float) min / 60));
            min -= h * 60;
            hours = h;
        }
        if (hours >= 24) {
            int d = Math.round(((float) hours / 24));
            hours -= d * 24;
            days = d;
        }
        return ((days > 0) ? (days + "d ") : "") + ((days > 0) ? (days + "d ") : "") + ((hours > 0) ? (hours + "h ") : "") + ((min > 0) ? (min + "m ") : "") + "s";
    }

    public static Long parseTime(String time) {
        long finalTime = 0;

        String daysPattern = "(\\d+)d";
        String hoursPattern = "(\\d+)h";
        String minPattern = "(\\d+)m";
        String secPattern = "(\\d+)s";

        if(!(time.matches(daysPattern) || time.matches(hoursPattern) || time.matches(minPattern) || time.matches(secPattern))) return 0L;

        if (time.matches(daysPattern)) {
            String dniStr = time.replaceAll(daysPattern, "$1");
            long dni = Long.parseLong(dniStr);
            finalTime += dni * 24 * 60 * 60 * 1000;
        }

        if (time.matches(hoursPattern)) {
            String dniStr = time.replaceAll(hoursPattern, "$1");
            long dni = Long.parseLong(dniStr);
            finalTime += dni * 60 * 60 * 1000;
        }

        if (time.matches(minPattern)) {
            String minutyStr = time.replaceAll(minPattern, "$1");
            long minuty = Long.parseLong(minutyStr);
            finalTime += minuty * 60 * 1000;
        }

        if (time.matches(secPattern)) {
            String sekundyStr = time.replaceAll(secPattern, "$1");
            long sekundy = Long.parseLong(sekundyStr);
            finalTime += sekundy * 1000;
        }

        return finalTime;
    }

}
