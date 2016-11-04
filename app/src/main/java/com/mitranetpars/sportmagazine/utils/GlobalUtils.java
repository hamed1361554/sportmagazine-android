package com.mitranetpars.sportmagazine.utils;

/**
 * Created by Hamed on 11/4/2016.
 */

public class GlobalUtils {
    public static boolean validateNationalCode(String nationalCode) {
        String code = StringUtils.padLeft(nationalCode, 10, "0");

        int sum = 0;
        for (int i = 9; i >= 1; i--){
            sum += Integer.parseInt(String.valueOf(code.toCharArray()[i])) * (i + 1);
        }

        int residue = sum % 11;
        if (residue >= 2) residue = 11 - residue;

        return Integer.parseInt(String.valueOf(code.toCharArray()[0])) == residue;
    }
}
