package com.adurosmart.utils;

/**
 * Created by best on 2016/7/8.
 */
public class Bytes {
    public static final long MAX_VALUE_U_INT_32 = (long) Math.pow(2, 32) - 1;
    public static final int MAX_VALUE_U_INT_16 = (int) (Math.pow(2, 16) - 1);
    public static final int MAX_VALUE_U_INT_8 = (int) (Math.pow(2, 8) - 1);
    public static final String MAC_ADDRESS_REGEX = "^([0-9A-Fa-f]{2}[:-]?){5}([0-9A-Fa-f]{2})$"; // FIXME: We also accept the - character?

    /**
     * Parses a Java int to an u_int8.
     */
    public static byte toUint8(int value) {
        if (value < 0 || value > MAX_VALUE_U_INT_8) {
            throw new IllegalArgumentException("Invalid u_int8. Please enter a value between 0 and " + MAX_VALUE_U_INT_8);
        }
        return (byte) (value & 0xFF);
    }

    /**
     * Parses a Java long to an u_int8.
     */
    public static byte toUint8(long value) {
        if (value < 0 || value > MAX_VALUE_U_INT_32) {
            throw new IllegalArgumentException("Invalid u_int32. Please enter a value between 0 and " + MAX_VALUE_U_INT_32);
        }
        return (byte) (value & 0xFF);
    }

    /**
     * Parses an u_int8 to a Java int.
     */
    public static int toInt(byte a) {
        return a & 0xFF;
    }

    /**
     * Parses an u_int16 to a Java int.
     *
     * @param a least significant byte
     * @param b most significant byte
     */
    public static int toInt(byte a, byte b) {
        return (((b & 0xFF) << 8) | (a & 0xFF)) & 0x0000FFFF;
    }

    public static int toInt(byte a, byte b, byte c, byte d) {
        return (((d & 0xFF) << 24) | ((c & 0xFF) << 16) | ((b & 0xFF) << 8) | (a & 0xFF)) & 0x0000FFFF;
    }

    /**
     * Parses an u_int32 to a Java long.
     *
     * @param a most significant byte
     * @param d least significant byte
     */
    public static long toLong(byte a, byte b, byte c, byte d) {
        return (((d & 0xFF) << 24) | ((c & 0xFF) << 16) | ((b & 0xFF) << 8) | (a & 0xFF)) & 0x00000000FFFFFFFFL;
    }

    /**
     * Converts a hexadecimal mac address to bytes. <p/>
     * Note: Lowercase characters [a-f] are supported.
     *
     * @param macAddress like FF:FF:FF:FF:FF:FF (Standard IEEE 802 format for MAC-48)
     * @return 6 bytes representing the mac address
     */
    public static byte[] macAddressToBytes(String macAddress) {

        if (!macAddress.matches(MAC_ADDRESS_REGEX)) {
            throw new IllegalArgumentException("Mac address must be like FF:FF:FF:FF:FF:FF, but is " + macAddress);
        }

        byte[] bytes = new byte[6];
        String[] address = macAddress.split(":"); // FIXME: We also accept the - character?
        for (int i = 0; i < address.length; i++) {
            bytes[i] = (byte) (Integer.parseInt(address[i], 16) & 0xFF);
        }
        return bytes;
    }

    protected final static char[] hexArray = "0123456789ABCDEF".toCharArray();

    public static String bytesToMacAddress(byte[] bytes) {

        if (bytes.length != 6) {
            throw new IllegalArgumentException("A mac address must have 6 bytes");
        }

        Character[] hexChars = new Character[bytes.length * 2];
        for (int j = 0; j < bytes.length; j++) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }
        return String.format("%C%C:%C%C:%C%C:%C%C:%C%C:%C%C", hexChars);
    }

    public static String bytesToHexString(byte[] bytes) {
        char[] hexChars = new char[bytes.length * 2];
        for (int j = 0; j < bytes.length; j++) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }
        return new String(hexChars);
    }

    public static byte[] hexStringToByteArray(String hexString) {
        int length = hexString.length();
        byte[] data = new byte[length / 2];
        for (int i = 0; i < length; i += 2) {
            data[i / 2] = (byte) ((Character.digit(hexString.charAt(i), 16) << 4)
                    + Character.digit(hexString.charAt(i + 1), 16));
        }
        return data;
    }

    public static byte[] concatenate(byte[]... arrays) {
        int length = 0;
        for (byte[] array : arrays) {
            length += array.length;
        }

        byte[] concatenated = new byte[length];
        for (int i = 0, cursor = 0; i < arrays.length; i++) {
            System.arraycopy(arrays[i], 0, concatenated, cursor, arrays[i].length);
            cursor += arrays[i].length;
        }
        return concatenated;
    }
}
