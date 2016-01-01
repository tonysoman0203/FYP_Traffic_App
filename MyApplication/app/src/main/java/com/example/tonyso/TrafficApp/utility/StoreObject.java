package com.example.tonyso.TrafficApp.utility;

/**
 * Created by NCH575 on 11/08/2015.
 */
public class StoreObject<T> {
    private boolean IsNeedEncode;
    private String key;
    private T value;

    public StoreObject(boolean isEncoded, String key, T value) {
        IsNeedEncode = isEncoded;
        this.key = key;
        this.value = value;
    }

    public boolean isNeedEncode() {
        return IsNeedEncode;
    }

    public String getKey() {
        return key;
    }

    public T getValue() {
        return value;
    }
}
