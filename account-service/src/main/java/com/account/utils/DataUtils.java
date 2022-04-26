package com.account.utils;

import java.util.Random;

public class DataUtils {
    public static String generateTempPwd(int length){
        String number ="0123456789";
        char otp[]=new char[length];
        Random getOtpNum= new Random();
        for (int i = 0; i <length ; i++) {
        otp[i]=number.charAt(getOtpNum.nextInt(number.length()));
        }
            String otpCpde="";
        for (int i = 0; i <otp.length ; i++) {
            otpCpde+=otp[i];
        }
        return  otpCpde;
        }
}
