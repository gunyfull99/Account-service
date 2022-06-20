package com.quiz.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class  DateTimeUtil {
    public static String DATETIME_FORMAT_1 = "yyyy-MM-dd HH:mm:ss";

    public static String convert(Date dateTime, String format){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat( format);
        return simpleDateFormat.format(dateTime);
    }
}
