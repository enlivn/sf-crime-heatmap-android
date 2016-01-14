package com.example.android.sfcrimeheatmap.helpers;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;

public class DateHelper {
    private static final String DATE_FORMAT_FOR_API = "yyyy-MM-dd\'T\'HH:mm:ss.SSS";
    private static final String DATE_FORMAT_FOR_DISPLAY = "MM/dd/yyyy";

    public static String dateToStringForDisplay(DateTime dateTime){
        return dateToString(dateTime, DATE_FORMAT_FOR_DISPLAY);
    }

    public static String dateToStringForApi(DateTime dateTime){
        return dateToString(dateTime, DATE_FORMAT_FOR_API);
    }

    private static String dateToString(DateTime dateTime, String dateFormat) {
        return DateTimeFormat.forPattern(dateFormat).print(dateTime);
    }
}
