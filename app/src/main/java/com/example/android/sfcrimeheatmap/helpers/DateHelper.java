package com.example.android.sfcrimeheatmap.helpers;

import android.content.Context;

import com.example.android.sfcrimeheatmap.R;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;

public class DateHelper {
    public static String getDateOneMonthBefore(Context context){
        DateTime dateTime = DateTime.now().minusMonths(1);
        return DateTimeFormat.forPattern(context.getString(R.string.date_format)).print(dateTime);
    }
}
