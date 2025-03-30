package com.urlShortener.util;

public class Base62Encoder {
    private static final String BASE62_CHARS = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";

    private static String encodeBase62(long num){
        StringBuffer sb = new StringBuffer();
        while(num>0){
            sb.append(BASE62_CHARS.charAt((int)(num%62)));
            num = num/62;
        }
        return sb.reverse().toString();
    }

    public static String generateShortCode(long id){
        String base62 = encodeBase62(id);
        return String.format("%7s",base62).replace(' ','0');
    }


}
