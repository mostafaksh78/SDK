package com.nexo.sdk;

import android.util.Log;

import com.google.android.gms.common.util.Base64Utils;

import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.ShortBufferException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class Crypto {
    static byte[] keyBytes = {0,1,2,3,4,5,6,7,0,1,2,3,4,5,6,7};
    static byte[] ivBytes = {0,0,0 ,0,0,0 ,0,0,0 ,0,0,0 ,0,0,0,0};
    private static final String key = "0123456701234567";
    private static final String initVector = "0000000000000000";
    public static byte[] encrypt(byte[] mes) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidAlgorithmParameterException, InvalidKeyException, ShortBufferException, BadPaddingException, IllegalBlockSizeException, UnsupportedEncodingException {
        IvParameterSpec iv = new IvParameterSpec(initVector.getBytes("UTF-8"));
        SecretKeySpec skeySpec = new SecretKeySpec(key.getBytes("UTF-8"), "AES");
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS7Padding");
        cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);
        byte[] encrypted= new byte[cipher.getOutputSize(mes.length)];
        int enc_len = cipher.update(mes, 0, mes.length, encrypted, 0);
        enc_len += cipher.doFinal(encrypted, enc_len);
        return encrypted;
    }
    public static String decrypt(String encrypted) throws Exception {
        byte[] decode ;
        decode = Base64Utils.decode(encrypted);
        Log.i("decryptBymostafa","decode size " + decode.length);
        IvParameterSpec iv = new IvParameterSpec(initVector.getBytes("UTF-8"));
        SecretKeySpec skeySpec = new SecretKeySpec(key.getBytes("UTF-8"), "AES");

        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS7Padding ");
        cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);


        byte[] original = cipher.doFinal(decode);

        return new String(original);
    }
}
