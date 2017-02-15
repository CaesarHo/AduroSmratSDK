package com.core.utils;

import android.content.Context;
import android.graphics.Color;
import android.telephony.TelephonyManager;

import com.core.global.Constants;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.net.InetAddress;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Pattern;

import static android.content.Context.TELEPHONY_SERVICE;

/**
 * Created by best on 2016/7/14.
 */
public class Utils {
    public static boolean isNumeric(String str) {
        Pattern pattern = Pattern.compile("[0-9]*");
        return pattern.matcher(str).matches();
    }

    public static String ConvertTimeByLong(long time) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Date date = new Date(time);
        return sdf.format(date);
    }

    /**
     * 获取主机IP
     *
     * @return
     */
    public static String getLocalhostip() {
        try {
            InetAddress thisIp = InetAddress.getLocalHost();
            return thisIp.getHostAddress();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String getFormatTellDate(String time) {

        String year = "-";

        SimpleDateFormat sd = new SimpleDateFormat("yyyy" + year + "MM" + year + "dd" + "" + " HH:mm:ss");
        Date dt = null;
        try {
            dt = new Date(Long.parseLong(time));
        } catch (Exception e) {
        }

        String s = "";
        if (dt != null) {
            s = sd.format(dt);
        }

        return s;
    }

    /**
     * 计算命令长度
     *
     * @param serial_type
     * @return
     */
    public static int cDataByteLen(String serial_type) {
        int commandDataByte_Len = 0;
        if (serial_type != null) {
            switch (serial_type) {
                case "0092"://开关
                    //10 + 40
                    commandDataByte_Len = 50;
                    break;
                case "0081"://亮度
                    //10 + 43
                    commandDataByte_Len = 53;
                    break;
                case "00B6"://颜色
                    commandDataByte_Len = 54;
                    //10 + 44
                    break;
                case "00C0"://色温
                    commandDataByte_Len = 53;
                    //10 + 43
                    break;
                case "00A5"://调用场景
                    //10 + 42
                    commandDataByte_Len = 52;
                    break;
            }
        }

        return commandDataByte_Len;
    }

    public static boolean isK6(byte[] bt){
        String isK64 = new String(bt);
        String  isK6 = isK64.substring(0, 2);
        if (isK6.equalsIgnoreCase("K6")){
            return true;
        }else{
            return false;
        }
    }

    /**
     * 获取手机唯一ID
     *
     * @param context 上下文
     * @return
     */
    public static String getIMEI(Context context) {
        TelephonyManager tm = (TelephonyManager) context.getSystemService(TELEPHONY_SERVICE);

      /*
       * 电话状态：
       * 1.tm.CALL_STATE_IDLE=0          无活动
       * 2.tm.CALL_STATE_RINGING=1  响铃
       * 3.tm.CALL_STATE_OFFHOOK=2  摘机
       */
        tm.getCallState();//int

      /*
       * 电话方位：
       *
       */
        tm.getCellLocation();//CellLocation

      /*
       * 唯一的设备ID：
       * GSM手机的 IMEI 和 CDMA手机的 MEID.
       * Return null if device ID is not available.
       */
        tm.getDeviceId();//String

      /*
       * 设备的软件版本号：
       * 例如：the IMEI/SV(software version) for GSM phones.
       * Return null if the software version is not available.
       */
        tm.getDeviceSoftwareVersion();//String

      /*
       * 手机号：
       * GSM手机的 MSISDN.
       * Return null if it is unavailable.
       */
        tm.getLine1Number();//String

      /*
       * 附近的电话的信息:
       * 类型：List<NeighboringCellInfo>
       * 需要权限：android.Manifest.permission#ACCESS_COARSE_UPDATES
       */
        tm.getNeighboringCellInfo();//List<NeighboringCellInfo>

      /*
       * 获取ISO标准的国家码，即国际长途区号。
       * 注意：仅当用户已在网络注册后有效。
       *       在CDMA网络中结果也许不可靠。
       */
        tm.getNetworkCountryIso();//String

      /*
       * MCC+MNC(mobile country code + mobile network code)
       * 注意：仅当用户已在网络注册时有效。
       *    在CDMA网络中结果也许不可靠。
       */
        tm.getNetworkOperator();//String

      /*
       * 按照字母次序的current registered operator(当前已注册的用户)的名字
       * 注意：仅当用户已在网络注册时有效。
       *    在CDMA网络中结果也许不可靠。
       */
        tm.getNetworkOperatorName();//String

      /*
       * 当前使用的网络类型：
       * 例如： NETWORK_TYPE_UNKNOWN  网络类型未知  0
         NETWORK_TYPE_GPRS     GPRS网络  1
         NETWORK_TYPE_EDGE     EDGE网络  2
         NETWORK_TYPE_UMTS     UMTS网络  3
         NETWORK_TYPE_HSDPA    HSDPA网络  8
         NETWORK_TYPE_HSUPA    HSUPA网络  9
         NETWORK_TYPE_HSPA     HSPA网络  10
         NETWORK_TYPE_CDMA     CDMA网络,IS95A 或 IS95B.  4
         NETWORK_TYPE_EVDO_0   EVDO网络, revision 0.  5
         NETWORK_TYPE_EVDO_A   EVDO网络, revision A.  6
         NETWORK_TYPE_1xRTT    1xRTT网络  7
       */
        tm.getNetworkType();//int

      /*
       * 手机类型：
       * 例如： PHONE_TYPE_NONE  无信号
         PHONE_TYPE_GSM   GSM信号
         PHONE_TYPE_CDMA  CDMA信号
       */
        tm.getPhoneType();//int

      /*
       * Returns the ISO country code equivalent for the SIM provider's country code.
       * 获取ISO国家码，相当于提供SIM卡的国家码。
       *
       */
        tm.getSimCountryIso();//String

      /*
       * Returns the MCC+MNC (mobile country code + mobile network code) of the provider of the SIM. 5 or 6 decimal digits.
       * 获取SIM卡提供的移动国家码和移动网络码.5或6位的十进制数字.
       * SIM卡的状态必须是 SIM_STATE_READY(使用getSimState()判断).
       */
        tm.getSimOperator();//String

      /*
       * 服务商名称：
       * 例如：中国移动、联通
       * SIM卡的状态必须是 SIM_STATE_READY(使用getSimState()判断).
       */
        tm.getSimOperatorName();//String

      /*
       * SIM卡的序列号：
       * 需要权限：READ_PHONE_STATE
       */
        tm.getSimSerialNumber();//String

      /*
       * SIM的状态信息：
       *  SIM_STATE_UNKNOWN          未知状态 0
       SIM_STATE_ABSENT           没插卡 1
       SIM_STATE_PIN_REQUIRED     锁定状态，需要用户的PIN码解锁 2
       SIM_STATE_PUK_REQUIRED     锁定状态，需要用户的PUK码解锁 3
       SIM_STATE_NETWORK_LOCKED   锁定状态，需要网络的PIN码解锁 4
       SIM_STATE_READY            就绪状态 5
       */
        tm.getSimState();//int

      /*
       * 唯一的用户ID：
       * 例如：IMSI(国际移动用户识别码) for a GSM phone.
       * 需要权限：READ_PHONE_STATE
       */
        tm.getSubscriberId();//String

      /*
       * 取得和语音邮件相关的标签，即为识别符
       * 需要权限：READ_PHONE_STATE
       */
        tm.getVoiceMailAlphaTag();//String

      /*
       * 获取语音邮件号码：
       * 需要权限：READ_PHONE_STATE
       */
        tm.getVoiceMailNumber();//String

      /*
       * ICC卡是否存在
       */
        tm.hasIccCard();//boolean

      /*
       * 是否漫游:
       * (在GSM用途下)
       */
        tm.isNetworkRoaming();//

        return tm.getDeviceId();
    }

    // 把二进制文件读入字节数组，如果没有内容，字节数组为null
    public static byte[] read(String filePath) {
        byte[] data = null;
        try {
            BufferedInputStream in = new BufferedInputStream(
                    new FileInputStream(filePath));
            try {
                data = new byte[in.available()];
                in.read(data);
            } finally {
                in.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return data;
    }

}
