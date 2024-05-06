package com.example.debatazo.usuario.md5;


import com.google.firebase.crashlytics.buildtools.reloc.org.apache.commons.codec.binary.Hex;

import java.security.MessageDigest;

/**
 * @Author 魏一鹤
 * @Description 将明文密码进行MD5加盐加密
 * @Date 23:18 2023/2/7
 **/
public class SaltMD5Util {

    /**
     * @Author 魏一鹤
     * @Description 生成盐和加盐后的MD5码，并将盐混入到MD5码中,对MD5密码进行加强
     * @Date 23:17 2023/2/7
     **/
    public static String generateSaltPassword(String password) {
        final String SALT = "2%7Edefault%7ECTRLIST%7";
        //将盐加到明文中，并生成新的MD5码
        password = md5Hex(password + SALT);
        //将盐混到新生成的MD5码中，之所以这样做是为了后期更方便的校验明文和秘文，也可以不用这么做，不过要将盐单独存下来，不推荐这种方式
        char[] cs = new char[48];
        for (int i = 0; i < 48; i += 3) {
            cs[i] = password.charAt(i / 3 * 2);
            char c = SALT.charAt(i / 3);
            cs[i + 1] = c;
            cs[i + 2] = password.charAt(i / 3 * 2 + 1);
        }
        return new String(cs);
    }

    /**
     * @Author 魏一鹤
     * @Description 生成MD5密码
     * @Date 23:16 2023/2/7
     **/
    private static String md5Hex(String src) {
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            byte[] bs = md5.digest(src.getBytes());
            return new String(new Hex().encode(bs));
        } catch (Exception e) {
            return null;
        }
    }

}


