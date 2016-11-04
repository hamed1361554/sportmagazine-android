package com.mitranetpars.sportmagazine.utils;

/**
 * Created by Hamed on 11/4/2016.
 */

public class StringUtils {
    public static String padLeft(String unpadded, int length, String padder) {
        if (unpadded.length() >= length) {
            return unpadded;
        }

        String repeated = new String(new char[length]).replace("\0", padder);
        return repeated.substring(unpadded.length()) + unpadded;
    }

    public static String padRight(String unpadded, int length, String padder) {
        if (unpadded.length() >= length) {
            return unpadded;
        }

        String repeated = new String(new char[length]).replace("\0", padder);
        return unpadded + repeated.substring(unpadded.length());
    }
}
