package com.adurosmart.utils;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

/**
 * Created by best on 2016/6/30.
 */
public class AESUtils {
    public static final String TAG = "AESUtils";
    public static final String ALGORITHM = "AES";
    public static final String MODE = "CBC";
    public static final String PADDING = "PKCS5Padding";
    public static final String TRANSFORMATION = String.format("%s/%s/%s", ALGORITHM, MODE, PADDING);

//    public static String encrypt(String seed, String clearText) {
//        Log.d(TAG, "加密前的seed=" + seed + ",内容为:" + clearText);
//        byte[] result = null;
//        try {
//            byte[] rawkey = getRawKey(seed.getBytes());
//            result = encrypt(rawkey, clearText.getBytes());
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        String content = toHex(result);
//        Log.d(TAG, "加密后的内容为:" + content);
//        return content;
//    }
//
//    public static String decrypt(String seed, String encrypted) {
//        Log.d(TAG, "解密前的seed=" + seed + ",内容为:" + encrypted);
//        byte[] rawKey;
//        try {
//            rawKey = getRawKey(seed.getBytes());
//            byte[] enc = toByte(encrypted);
//            byte[] result = decrypt(rawKey, enc);
//            String coentn = new String(result);
//            Log.d(TAG, "解密后的内容为:" + coentn);
//            return coentn;
//        } catch (Exception e) {
//            e.printStackTrace();
//            return null;
//        }
//    }
//
//    private static byte[] getRawKey(byte[] seed) throws Exception {
//        KeyGenerator kgen = KeyGenerator.getInstance("AES");
//        SecureRandom sr = SecureRandom.getInstance("SHA1PRNG");
//        sr.setSeed(seed);
//        kgen.init(128, sr);
//        SecretKey sKey = kgen.generateKey();
//        byte[] raw = sKey.getEncoded();
//
//        return raw;
//    }
//
//    private static byte[] encrypt(byte[] raw, byte[] clear) throws Exception {
//        SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
////        Cipher cipher = Cipher.getInstance("AES");
//        Cipher cipher = Cipher.getInstance("AES");
//        cipher.init(Cipher.ENCRYPT_MODE, skeySpec, new IvParameterSpec(new byte[cipher.getBlockSize()]));
//        byte[] encrypted = cipher.doFinal(clear);
//        return encrypted;
//    }
//
//    /**
//     * 根据密钥解密已经加密的数据
//     *
//     * @param raw
//     * @param encrypted
//     * @return
//     * @throws Exception
//     */
//    private static byte[] decrypt(byte[] raw, byte[] encrypted) throws Exception {
//        SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
////        Cipher cipher = Cipher.getInstance("AES");
//        Cipher cipher = Cipher.getInstance("AES");
//        cipher.init(Cipher.DECRYPT_MODE, skeySpec, new IvParameterSpec(new byte[cipher.getBlockSize()]));
//        byte[] decrypted = cipher.doFinal(encrypted);
//        return decrypted;
//    }
//
//    public static String toHex(String txt) {
//        return toHex(txt.getBytes());
//    }
//
//    public static String fromHex(String hex) {
//        return new String(toByte(hex));
//    }
//
//    public static byte[] toByte(String hexString) {
//        int len = hexString.length() / 2;
//        byte[] result = new byte[len];
//        for (int i = 0; i < len; i++)
//            result[i] = Integer.valueOf(hexString.substring(2 * i, 2 * i + 2), 16).byteValue();
//        return result;
//    }
//
//    public static String toHex(byte[] buf) {
//        if (buf == null)
//            return "";
//        StringBuffer result = new StringBuffer(2 * buf.length);
//        for (int i = 0; i < buf.length; i++) {
//            appendHex(result, buf[i]);
//        }
//        return result.toString();
//    }
//
//    private static void appendHex(StringBuffer sb, byte b) {
//        final String KEY = "1234567812345678";
//        sb.append(KEY.charAt((b >> 4) & 0x0f)).append(KEY.charAt(b & 0x0f));
//    }










    /**
     * 加密
     *
     * @param content 需要加密的内容
     * @param password  加密密码
     * @return
     */
    public static byte[] encrypt(String content, String password) {
        try {
            KeyGenerator kgen = KeyGenerator.getInstance("AES");
            kgen.init(128, new SecureRandom(password.getBytes()));
            SecretKey secretKey = kgen.generateKey();
            byte[] enCodeFormat = secretKey.getEncoded();
            SecretKeySpec key = new SecretKeySpec(enCodeFormat, "AES");
            Cipher cipher = Cipher.getInstance("AES");// 创建密码器
            byte[] byteContent = content.getBytes("utf-8");
            cipher.init(Cipher.ENCRYPT_MODE, key);// 初始化
            byte[] result = cipher.doFinal(byteContent);
            return result; // 加密
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        }
        return null;
    }
    /**解密
     * @param content  待解密内容
     * @param password 解密密钥
     * @return
     */
    public static byte[] decrypt(byte[] content, String password) {
        try {
            KeyGenerator kgen = KeyGenerator.getInstance("AES");
            kgen.init(256, new SecureRandom(password.getBytes()));
            SecretKey secretKey = kgen.generateKey();
            byte[] enCodeFormat = secretKey.getEncoded();
            SecretKeySpec key = new SecretKeySpec(enCodeFormat, "AES");
            Cipher cipher = Cipher.getInstance("AES");// 创建密码器
            cipher.init(Cipher.DECRYPT_MODE, key);// 初始化
            byte[] result = cipher.doFinal(content);
            return result; // 加密
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        }
        return null;
    }
}
