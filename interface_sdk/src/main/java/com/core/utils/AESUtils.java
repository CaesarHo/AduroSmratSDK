package com.core.utils;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

/**
 * Created by best on 2016/11/12.
 */

public class AESUtils {
    //注意: 这里的password(秘钥必须是16位的)
    private static final String keyBytes = "B73F1AC4F3F8DB98";
    private static final String algorithmStr = "AES/CBC/PKCS5Padding";

    /**
     * 加密
     */
    public static byte[] encode(byte[] content) {
        //加密之后的字节数组,转成16进制的字符串形式输出
        return parseByte2HexStr(encrypt(content, keyBytes));
    }

    /**
     * 解密
     */
    public static byte[] decode(byte[] content) {
        //解密之前,先将输入的字符串按照16进制转成二进制的字节数组,作为待解密的内容输入
        byte[] b = decrypt(parseHexStr2Byte(content), keyBytes);
        return b;
    }


    /**
     * 将二进制转换成16进制
     *
     * @param buf
     * @return
     */
    public static byte[] parseByte2HexStr(byte buf[]) {
        byte[] bytes = buf;
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < buf.length; i++) {
            String hex = Integer.toHexString(buf[i] & 0xFF);
            if (hex.length() == 1) {
                hex = '0' + hex;
            }
            sb.append(hex.toUpperCase());
        }
        return bytes;
    }

    /**
     * 将16进制转换为二进制
     *
     * @param hexStr
     * @return
     */
    public static byte[] parseHexStr2Byte(byte[] hexStr) {
        byte[] result = hexStr;
//        if (hexStr.length < 1)
//            return null;
//        byte[] result = new byte[hexStr.length / 2];
//        for (int i = 0; i < hexStr.length / 2; i++) {
////            int high = Integer.parseInt(hexStr.substring(i * 2, i * 2 + 1), 16);
////            int low = Integer.parseInt(hexStr.substring(i * 2 + 1, i * 2 + 2), 16);
//            result[i] = (result[i]);
//        }
        return result;
    }

    private static byte[] encrypt(byte[] content, String password) {
        try {
            byte[] keyStr = getKey(password);
            SecretKeySpec key = new SecretKeySpec(keyStr, "AES");
            Cipher cipher = Cipher.getInstance(algorithmStr);//algorithmStr
            byte[] byteContent = content;
            cipher.init(Cipher.ENCRYPT_MODE, key);
            byte[] result = cipher.doFinal(byteContent);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private static byte[] decrypt(byte[] content, String password) {
        try {
            byte[] keyStr = getKey(password);
            SecretKeySpec key = new SecretKeySpec(keyStr, "AES");
            Cipher cipher = Cipher.getInstance(algorithmStr);//algorithmStr
            cipher.init(Cipher.DECRYPT_MODE, key);
            byte[] result = cipher.doFinal(content);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    private static byte[] getKey(String password) {
        byte[] rByte = null;
        if (password != null) {
            rByte = password.getBytes();
        } else {
            rByte = new byte[24];
        }
        return rByte;
    }
}
