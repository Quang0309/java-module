package com.facebook.profilo.sample.model;

import java.util.Random;

public class GenID {
    static private char[] BASE64_ALPHABET = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/".toCharArray();
    static String new_id()
    {
        Random ran = new Random();
        long num = ran.nextInt(Integer.MAX_VALUE-1)+1;

        num += ran.nextInt(Integer.MAX_VALUE); //get an unsigned int
        char[] data = {'A','A','A','A','A','A','A','A','A','A','A'};
        int index = data.length-1;
        while(num!=0&&index>=0)
        {
            int denom = (int) (num % 64);
            num= num/8;
            data[index] = BASE64_ALPHABET[denom];
            index--;
        }
        return new String(data);
    }
}
