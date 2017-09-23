package com.adurosmart.util;

import java.text.SimpleDateFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by best on 2016/10/17.
 */

public class StringUtil {
    public static boolean isNumeric(String str) {
        Pattern pattern = Pattern.compile("[0-9]*");
        return pattern.matcher(str).matches();
    }

    public static boolean isDecimalInteger(String str) {
        /*
         * ^\\d+$ 是判断位正整数的 ^\\d+\\.\\d+$ 判断是否位正小数 -\\d+$：判断是否位负整
		 ?
         */

        // 判断是否为正整数或正小数
        Pattern pattern = Pattern.compile("^\\d+$|^\\d+\\.\\d+$");
        return pattern.matcher(str).matches();
    }

    public static boolean isDate(String str, String dateFormat) {
        if (str != null) {
            SimpleDateFormat formatter = new SimpleDateFormat(dateFormat);
            formatter.setLenient(false);
            try {
                formatter.format(formatter.parse(str));
            } catch (Exception e) {
                return false;
            }
            return true;
        }
        return false;
    }

    public static boolean isDate(String str) {
        String dateFormat = "yyyy-MM-dd";
        if (str != null) {
            SimpleDateFormat formatter = new SimpleDateFormat(dateFormat);
            formatter.setLenient(false);
            try {
                return str.equals(formatter.format(formatter.parse(str)));

            } catch (Exception e) {
                return false;
            }
        }
        return false;
    }

    public static boolean isDateTime(String str) {
        String dateFormat = "yyyy-MM-dd HH:mm:ss";
        if (str != null) {
            SimpleDateFormat formatter = new SimpleDateFormat(dateFormat);
            formatter.setLenient(false);
            try {
                return str.equals(formatter.format(formatter.parse(str)));
            } catch (Exception e) {
                return false;
            }
        }
        return false;
    }

    public static boolean isMonth(String str) {
        String dateFormat = "yyyy-MM";
        if (str != null) {
            SimpleDateFormat formatter = new SimpleDateFormat(dateFormat);
            formatter.setLenient(false);
            try {
                return str.equals(formatter.format(formatter.parse(str)));
            } catch (Exception e) {
                return false;
            }

        }
        return false;
    }

    public static String[] getArraysNum(String[][] Phones) throws Exception {
        try {
            for (int i = 0; i <= Phones[i].length; i++) {
                String[] b = new String[Phones[i].length];
                b[i] = String.valueOf(Phones[i]);
                return b;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static boolean isYear(String str) {
        String dateFormat = "yyyy";
        if (str != null) {
            if (str.length() != 4) {
                return false;
            }
            SimpleDateFormat formatter = new SimpleDateFormat(dateFormat);
            formatter.setLenient(false);
            try {
                return str.equals(formatter.format(formatter.parse(str)));
            } catch (Exception e) {
                return false;
            }
        }
        return false;
    }

    public static boolean isEmail(String email) {
        String regex = "\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*";
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(email);
        return m.find();
    }

    public static boolean isPhone(String phone) {
        String[] regex = new String[]{
                "^0(10|2[0-9]{1}|[3-9][0-9]{2})[1-9][0-9]{6,7}(\\+[0-9]{1,6})?$",
                "^1(3|4|5|8{1})([0-9]{1})([0-9]{8})?$", "^[0-9\\-]{11,20}$",
                "^400(\\d){7,7}$", "^400(\\d){7,7}(\\+[0-9]{1,4})?$"};
        for (int i = 0; i < regex.length; i++) {
            Pattern p = Pattern.compile(regex[i]);
            Matcher m = p.matcher(phone);
            if (m.find()) {
                return true;
            }
        }

        return false;
    }

    // +86的不能打
    public static boolean isPhone86(String phone) {
        String[] regex = new String[]{"^86?",};
        for (int i = 0; i < regex.length; i++) {
            Pattern p = Pattern.compile(regex[i]);
            Matcher m = p.matcher(phone);
            if (m.find()) {
                return true;
            }
        }
        return false;
    }

    // 是否可以拨打回拨的号码
    public static boolean isCallbackPhone(String phone) {
        String[] regex = new String[]{
                "^0(10|2[0-9]{1}|[3-9][0-9]{2})[1-9][0-9]{6,7}(\\+[0-9]{1,6})?$",
                "^1(3|4|5|8{1})([0-9]{1})([0-9]{8})?$"};
        for (int i = 0; i < regex.length; i++) {
            Pattern p = Pattern.compile(regex[i]);
            Matcher m = p.matcher(phone);
            if (m.find()) {
                return true;
            }
        }
        return false;
    }

    // /^13\d{9}$/gi手机号正则表达式
    public static boolean isMobilePhone(String phone) {
        String regex = "^1(3|4|5|8{1})([0-9]{1})([0-9]{8})?$";
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(phone);
        return m.find();
    }

    public static boolean isUsername(String username) {
        String regex = "^[a-zA-Z0-9_]{3,20}$";
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(username);
        return m.find();
    }

    public static boolean isPassword(String password) {
        String regex = "^[a-zA-Z0-9]{6,20}$";
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(password);
        return m.find();
    }

    // 判断是否是字母和数字组成
    public static boolean isNumOrLetter(String str) {
        String regex = "^[0-9a-zA-Z]+$";
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(str);
        return m.find();
    }

    public static String subZeroAndDot(String s) {
        if (s.indexOf(".") > 0) {
            s = s.replaceAll("0+?$", "");// 去掉多余?
            s = s.replaceAll("[.]$", "");// 如最后一位是.则去掉
        }
        return s;
    }

    // 判断是否是汉字、字母、数字和下划线组成
    public static boolean isChineseOrNumOrLetter(String str) {
        String regex = "^[0-9a-zA-Z_\u4e00-\u9fa5]+$";
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(str);
        return m.find();
    }

    // 去掉电话号码中的空格
    public static String replaceBlank(String txtPhoneDial) {
        String dest = "";
        if (txtPhoneDial != null) {
            Pattern p = Pattern.compile("\\s*|\t|\r|\n");
            Matcher m = p.matcher(txtPhoneDial);
            dest = m.replaceAll("");
        }
        return dest;
    }

    // 去掉电话中的空格和“-”
    public static String replaceAll(String txtPhoneDial) {
        String dest = "";
        if (txtPhoneDial != null) {
            dest = txtPhoneDial.replaceAll("-", "").replaceAll(" ", "");
        }
        return dest;
    }

    // 去掉电话号码中的非数字字符和+86
    public static String replaceUnPhoneNumber(String str) {
        if (str == null) {
            return "";
        }
        // 只允许数字
        String regEx = "(^[+]86)|([^0-9])";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(str);
        return m.replaceAll("").trim();
    }

    public static int getRandomNumber() {
        int Min = 100000;
        int Max = 10 * Min;
        int number = (int) (Min + Math.random() * (Max - Min - 1));
        return number;
    }

    public static void main(String[] args) {
        System.out.println("isUsername33:" + isUsername("dennis_19"));
        System.out.println("isPassword:" + isPassword("222dd22"));
        System.out.println("isPhone:" + isPhone("075533445566"));
        System.out.println("isPhone:" + isPhone("0755-33445566"));
        System.out.println("isPhone:" + isPhone("4007160988+1233"));
        System.out.println("isMonth:" + isMonth("2011-01"));
        System.out.println("isDateTime:" + isDateTime("2011-01-01 00:00:00"));
        System.out.println("isDate:" + isDate("2011-01-01"));
        System.out.println("isYear:" + isYear("2222"));

        System.out.println("isEmail:" + isEmail("dennis123allywll.com"));
        System.out.println("isDecimalInteger:" + isDecimalInteger("1.5d"));
        String regex = "<(\\w+)>(\\w+)</(\\w+)>";
        Pattern pattern = Pattern.compile(regex);

        // String
        // input="<name>Bill</name><salary>50000</salary><title>GM</title>";
        String input = "<body><id>5</id><name>Bill</name><salary>50000</salary><title>GM</title></body>";
        System.out.println(input);
        Matcher matcher = pattern.matcher(input);

        while (matcher.find()) {
            System.out.println(matcher.group(1) + "," + matcher.group(2) + ","
                    + matcher.group(3));
        }
    }
}
