package com.example.cashrichdata.generation;

import org.apache.tomcat.util.codec.binary.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import java.net.URLEncoder;
import java.security.MessageDigest;

public class EncryptionDecryption {
    private static final String key1 = "1234567890123456";
    private static final String key2 = "1234567890123456";
    public static String encryptAndEncode(String value) {
        StringBuffer hexString = new StringBuffer();
        try
        {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(value.getBytes());
            byte byteData[] = md.digest();

            for (int i=0;i<byteData.length;i++) {
                String hex=Integer.toHexString(0xff & byteData[i]);
                if(hex.length()==1) hexString.append('0');
                hexString.append(hex);
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        return hexString.toString();

    }
}
