package com.adurosmart.util;

import android.net.ParseException;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 日期显示格式
 * Created by best on 2016/10/17.
 */
public class DateUtil
{
    public static String getDateTime()
    {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
    }

    public static String getDate()
    {
        return new SimpleDateFormat("yyyy-MM-dd").format(new Date());
    }

    public static String nowToString(String format)
    {
        return new SimpleDateFormat(format).format(new Date());
    }

    public static String dateToString(String format, String strDate)
    {
        String date = null;
        try
        {
            date = new SimpleDateFormat(format).format(new SimpleDateFormat(
                    ConstsDefine.DateFormat.DateYYYYMDHMS).parse(strDate));
        }
        catch (ParseException ex)
        {
            ex.printStackTrace();
        }
        finally
        {
            return date;
        }
    }

    public static String formatDate(String format, String strDate)
    {
        String formatDate = strDate;
        SimpleDateFormat formatter = new SimpleDateFormat(format);
        formatter.setLenient(false);
        try
        {
            formatDate = formatter.format(formatter.parse(strDate));

        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
        finally
        {
            return formatDate;
        }

    }

    public static Date stringToDate(String strDate, String format)
    {
        Date date = null;
        try
        {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
            date = simpleDateFormat.parse(strDate);
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
        return date;
    }

    public static Date stringToDateTime(String strDate)
    {
        Date date = null;
        try
        {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(
                    ConstsDefine.DateFormat.DateYYYYMDHMS);
            date = simpleDateFormat.parse(strDate);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return date;
    }

    public static Date stringToDate(String strDate)
    {
        Date date = null;
        try
        {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(
                    ConstsDefine.DateFormat.DayYYYYMD);
            date = simpleDateFormat.parse(strDate);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return date;
    }

    public static String getCurrMonthStartDate()
    {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM");
        String strDate = format.format(new Date()) + "-01";
        return strDate;
    }

    public static String getDiffDate(Calendar cal, int diffDays)
    {
        cal.add(Calendar.DAY_OF_MONTH, diffDays);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        String strDate = format.format(cal.getTime());
        return strDate;
    }

    public static String getDiffDateYMD(Calendar cal, int diffDays)
    {
        cal.add(Calendar.DAY_OF_MONTH, diffDays);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String strDate = format.format(cal.getTime());
        return strDate;
    }

    public static String getDiffMonth(Calendar cal, int diffMonths)
    {
        cal.add(Calendar.MONTH, diffMonths);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String strDate = format.format(cal.getTime());
        return strDate;
    }

    public static String getDiffDateBySeconds(Calendar cal, int diffSends)
    {
        cal.add(Calendar.SECOND, diffSends);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String strDate = format.format(cal.getTime());
        return strDate;
    }

    public static long getIntervalDates(Date date1, Date date2)
            throws ParseException
    {
        long interval = (date1.getTime() - date2.getTime()) / 1000;
        long day = interval / (24 * 3600);
        return day;
    }

    public static int getIntIntervalDates(Date intdate1, Date intdate2)
            throws ParseException
    {
        long interval = (intdate1.getTime() - intdate2.getTime()) / 1000;
        int day = (int) (interval / (24 * 3600));
        return day;
    }

    public static int getIntIntervalDates(String strDate1, String strDate2)
            throws ParseException
    {
        return getIntIntervalDates(stringToDate(strDate1),
                stringToDate(strDate2));
    }

    public static long getIntervalDates(String strDate1, String strDate2)
            throws ParseException
    {
        return getIntervalDates(stringToDateTime(strDate1),
                stringToDate(strDate2));
    }

    public static long getIntervalSeconds(String strDate1, String strDate2)
            throws ParseException
    {
        long interval = getIntervalSeconds(stringToDate(strDate1),
                stringToDate(strDate2));
        return interval;
    }

    public static long getIntervalSeconds(Date date1, Date date2)
    {
        long interval = (date2.getTime() - date1.getTime()) / 1000;
        long day = interval / (24 * 3600);
        long hour = interval % (24 * 3600) / 3600;
        long minute = interval % 3600 / 60;
        long second = interval % 60;
        return interval;
    }

    public static String getIntervalHMS(Date date1, Date date2)
    {
        long interval = (date2.getTime() - date1.getTime()) / 1000;
        return getIntervalHMS(interval);
    }

    public static String getIntervalHMS(long secondInterval)
    {
        long interval = secondInterval;
        long day = interval % (24 * 3600);
        long hour = interval % (24 * 3600) / 3600;
        long minute = interval % 3600 / 60;
        long second = interval % 60;
        hour = (day > 0) ? day * 24 + hour : hour;
        String sHour = (hour < 10) ? ("0" + hour) : String.valueOf(hour);
        String sMinute = (minute < 10) ? ("0" + minute) : String
                .valueOf(minute);
        String sSecond = (second < 10) ? ("0" + second) : String
                .valueOf(second);
        return sHour + ":" + sMinute + ":" + sSecond;

    }

    public static long getIntervalMillSeconds(Date date1, Date date2)
    {
        long interval = date2.getTime() - date1.getTime();
        return interval;
    }

    public static long getIntervalMinutes(Date date1, Date date2)
    {
        long interval = (date2.getTime() - date1.getTime()) / 1000;
        return getIntervalMinutes(interval);
    }

    public static int getIntervalMinutes(long intervalSecond)
    {
        long interval = intervalSecond;
        int minutes = (int) Math.ceil(interval / 60.0);
        return minutes;
    }

    public static long getIntervalHour(Date date1, Date date2)
    {
        long interval = (date2.getTime() - date1.getTime()) / 1000;
        long hours = (long) Math.ceil(interval / 3600.0);
        return hours;
    }

    public static Calendar stringToCalendar(String strDate)
    {
        Calendar cal = Calendar.getInstance();
        cal.setTime(DateUtil.stringToDateTime(strDate));
        return cal;
    }

    public static String[] getArrayStartEndDate(String sDate)
    {
        String[] arrayDate = new String[] { "", "" };
        String startDate = "";
        String endDate = "";
        if (sDate != null && !sDate.isEmpty())
        {
            String[] aDate = sDate.split(",");
            if (aDate.length > 0)
            {
                startDate = aDate[0];
                if (StringUtil.isDate(startDate))
                {
                    startDate += "00:00:00";
                }
                if (!StringUtil.isDate(startDate)
                        && !StringUtil.isDateTime(startDate))
                {
                    startDate = "";
                }
            }
            if (aDate.length >= 2)
            {
                endDate = aDate[1];
                if (StringUtil.isDate(endDate))
                {
                    startDate += "23:59:59";
                }
                if (!StringUtil.isDate(endDate)
                        && !StringUtil.isDateTime(endDate))
                {
                    endDate = "";
                }
            }
        }
        arrayDate[0] = startDate;
        arrayDate[1] = endDate;
        return arrayDate;
    }

    public static String getYMDDate(int year, int month, int day)
    {
        String sMonth = (month < 10) ? "0" + month : String.valueOf(month);
        String sDay = (day < 10) ? "0" + day : String.valueOf(day);
        String sDate = year + "-" + sMonth + "-" + sDay;
        return sDate;
    }

    public static String getYMDTime(int hour, int minute)
    {
        String sHour = (hour < 10) ? ("0" + hour) : String.valueOf(hour);
        String sMinute = (minute < 10) ? ("0" + minute) : String
                .valueOf(minute);
        String sTime = sHour + ":" + sMinute;
        return sTime;
    }
}

