package com.crowdo.p2pconnect.helpers;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by cwdsg05 on 27/3/17.
 */

public class HashingUtils {

    public static String hashSHA256(String msg){
        StringBuffer sb = new StringBuffer();
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(msg.getBytes());

            byte byteData[] = md.digest();

            //convert the byte to hex format method 1
            sb = new StringBuffer();
            for (int i = 0; i < byteData.length; i++) {
                sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
            }
        }catch (NoSuchAlgorithmException e){
            e.printStackTrace();
        }

        return sb.toString();
    }
}
