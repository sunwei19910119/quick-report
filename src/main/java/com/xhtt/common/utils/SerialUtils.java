package com.xhtt.common.utils;

public class SerialUtils {
    public static synchronized String getSerialNo(String noS, boolean b, String serialNo) {
        if (b) {
            String e = String.format("%03d", 1);
            noS = noS + e;
        } else {
            int n = Integer.valueOf(serialNo.substring(8));
            String e = String.format("%03d", n + 1);
            noS = noS + e;
        }
        return noS;
    }
}
