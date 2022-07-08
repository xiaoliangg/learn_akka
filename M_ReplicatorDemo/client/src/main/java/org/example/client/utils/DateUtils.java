package org.example.client.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @description: TODO
 * @author: xiaoliang
 * @date: 2022/7/8 10:55
 **/
public class DateUtils {
    public static final String COMMENDDATETIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
    public static final String COMMENDDATETIME_FORMAT2 = "yyyyMMddHHmmss";

    public static Date parseTimeDate(String dateStr) {
        Date date = null;
        SimpleDateFormat formater = new SimpleDateFormat(COMMENDDATETIME_FORMAT);
        try {
            date = formater.parse(dateStr);
        }
        catch (ParseException e) {
        }
        return date;
    }

    public static String getNowTime() {
        SimpleDateFormat f = new SimpleDateFormat(COMMENDDATETIME_FORMAT2);
        return f.format(new Date());
    }
}
