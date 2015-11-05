package com.example.tonyso.TrafficApp.utility.encryption;

/**
 * Created by NCH575 on 14/08/2015.
 */
public class IntentEncryptHelper {
    private String encrypt_key = "";
    private String decrypt_key = "";

    public IntentEncryptHelper() {
    }

    public IntentEncryptHelper(String encrypt_key, String decrypt_key) {
        this.encrypt_key = encrypt_key;
        this.decrypt_key = decrypt_key;
    }

    public CharSequence[] encryption(CharSequence[] data) {
        return data;
    }

    public CharSequence[] decryption(CharSequence[] data) {
        return data;
    }

    public String encryption(String data) {
        return data;
    }

    public String decryption(String data) {
        return data;
    }
}
