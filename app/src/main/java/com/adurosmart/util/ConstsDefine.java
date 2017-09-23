package com.adurosmart.util;

/**
 * Created by best on 2016/10/17.
 */

public class ConstsDefine {
    public interface Settings
    {
        public interface LogPath
        {
            String sdLogDir = "cru/log";
        }
    }

    public interface DateFormat
    {
        String YYYYMDHMS = "yyyyMMddHHmmss";
        String YYYYMDHMSsss = "yyyyMMddHHmmssSSS";
        String DateYYYYMDHMS = "yyyy-MM-dd HH:mm:ss";
        String DayYYYYMD = "yyyy-MM-dd";
        String MonthYYYYM = "yyyy-MM";
        String YearYYYY = "yyyy";
        String YYYYMD = "yyyyMMdd";
        String DateYYYYMDHMSsss = "yyyy-MM-dd HH:mm:ss.SSS";
    }

    public interface Handler
    {
        public interface Message
        {
            int HTTP_QUERY_LIST_SUCCESS = 0001;// 查询成功
            int HTTP_QUERY_LIST_FAIL = 0002;// 查询失败
        }
    }
}
