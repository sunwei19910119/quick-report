package com.xhtt.common.utils;

public class ByteToHexUtils {

    /**
     * 将十六进制的字符串转换成二进制的字符串
     *
     * @param hexString
     * @return
     */
    public static String hexStrToBinaryStr(String hexString) {

        if (hexString == null || hexString.equals("")) {
            return null;
        }
        StringBuffer sb = new StringBuffer();
        // 将每一个十六进制字符分别转换成一个四位的二进制字符
        for (int i = 0; i < hexString.length(); i++) {
            String indexStr = hexString.substring(i, i + 1);
            String binaryStr = Integer.toBinaryString(Integer.parseInt(indexStr, 16));
            while (binaryStr.length() < 4) {
                binaryStr = "0" + binaryStr;
            }
            sb.append(binaryStr);
        }

        return sb.toString();
    }

    /**
     * 二进制字符串转换为十六进制字符串
     * <p>
     * 二进制字符串位数必须满足是4的倍数
     *
     * @param binaryStr
     * @return
     */
    public static String binaryStrToHexStr(String binaryStr) {

        if (binaryStr == null || binaryStr.equals("") || binaryStr.length() % 4 != 0) {
            return null;
        }

        StringBuffer sbs = new StringBuffer();
        // 二进制字符串是4的倍数，所以四位二进制转换成一位十六进制
        for (int i = 0; i < binaryStr.length() / 4; i++) {
            String subStr = binaryStr.substring(i * 4, i * 4 + 4);
            String hexStr = Integer.toHexString(Integer.parseInt(subStr, 2));
            sbs.append(hexStr);
        }

        return sbs.toString();
    }

    /**
     * 把16进制字符串转换成字节数组
     * @param str
     */
    public static byte[] hexStrToBytes(String str) {
        if(str == null || str.trim().equals("")) {
            return new byte[0];
        }

        byte[] bytes = new byte[str.length() / 2];
        for(int i = 0; i < str.length() / 2; i++) {
            String subStr = str.substring(i * 2, i * 2 + 2);
            bytes[i] = (byte) Integer.parseInt(subStr, 16);
        }

        return bytes;
    }

    /**
     * 数组转成十六进制字符串
     * @param b
     * @return HexString
     */
    public static String bytesToHexStr(byte[] b){
        StringBuffer buffer = new StringBuffer();
        for (int i = 0; i < b.length; ++i){
            buffer.append(toHexString1(b[i]));
        }
        return buffer.toString();
    }
    public static String toHexString1(byte b){
        String s = Integer.toHexString(b & 0xFF);
        if (s.length() == 1){
            return "0" + s;
        }else{
            return s;
        }
    }

}
