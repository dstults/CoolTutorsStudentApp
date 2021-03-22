package org.cooltutors.student;

// Darren Stults

// I know these are "expired" but the operations are simple, not region-specifict,
// and these don't require Android version checking for backwards compatibility
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateHelpers {

    public static String getDayOfWeek(String dateMmDdYyyy) {
        SimpleDateFormat input = new SimpleDateFormat("MM/dd/yyyy");
        Date date;
        try {
             date = input.parse(dateMmDdYyyy);
        } catch (ParseException e) {
            return "???";
        }
        SimpleDateFormat output = new SimpleDateFormat("EEE");
        String dayOfWeek = output.format(date).substring(0, 3).toUpperCase();
        return dayOfWeek;
    }

    private static Date toDate(String dateString) {
        SimpleDateFormat input = new SimpleDateFormat("MM/dd/yyyy");
        Date dateDate;
        try {
            dateDate = input.parse(dateString);
        } catch (ParseException e) {
            return new Date();
        }
        return dateDate;
    }

    public static int compareDateStrings(String date1, String date2) {
        return toDate(date1).compareTo(toDate(date2));
    }

}
