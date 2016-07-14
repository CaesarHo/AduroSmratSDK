package com.interfacecallback;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by best on 2016/7/14.
 */
public class Utils {
    public static String ConvertTimeByLong(long time){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Date date = new Date(time);
        return sdf.format(date);
    }
}
