package com.example.tonyso.TrafficApp.utility.encryption;

import android.util.Base64;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

public class EncryptDecryptAES {
    //Important!!These setting is matching the encryption method with the App.
    private static final String ALGORITHM = "AES/ECB/PKCS5Padding";
    private static final String ENCRYPTIONKEY = "bVNIUjIwMTVLZXk=";

    public EncryptDecryptAES() {
    }

    public String encryptString(String valueToEnc) {
        try {
            Key key = generateKey();
            Cipher c = Cipher.getInstance(ALGORITHM);
            c.init(Cipher.ENCRYPT_MODE, key);
            byte[] encValue = c.doFinal(valueToEnc.getBytes("UTF-8"));
            String encryptedValue = Base64.encodeToString(encValue, 0, encValue.length, Base64.DEFAULT);
            return encryptedValue;
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        }
        return "";
    }

    public String decryptString(String encryptedValue) {
        try {
            Key key = generateKey();
            Cipher c = Cipher.getInstance(ALGORITHM);
            c.init(Cipher.DECRYPT_MODE, key);
            byte[] decordedValue = Base64.decode(encryptedValue, Base64.DEFAULT);
            byte[] decValue = c.doFinal(decordedValue);
            String decryptedValue = new String(decValue, "UTF-8");
            return decryptedValue;
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        }
        return "";
    }

    private Key generateKey() {
        byte[] keyValue;
        Key key = null;
        try {
            keyValue = ENCRYPTIONKEY.getBytes("UTF-8");
            key = new SecretKeySpec(keyValue, "AES");
            return key;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return key;
    }
}
