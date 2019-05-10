package com.project.group18.limberup;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Security {

    public static String hashPassword(String password)
    {

        StringBuilder sb = new StringBuilder();

        try{
            // Create MessageDigest instance for MD5
            MessageDigest md = MessageDigest.getInstance("MD5");
            //Add password bytes to digest
            md.update(password.getBytes());
            //Get the hash's bytes
            byte[] bytes = md.digest();
            //This bytes[] has bytes in decimal format;
            //Convert it to hexadecimal format

            for(int i = 0; i < bytes.length; i++)
            {
                sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
            }

        }catch(NoSuchAlgorithmException e)
        {
            e.printStackTrace();
        }
        return sb.toString();
    }
}