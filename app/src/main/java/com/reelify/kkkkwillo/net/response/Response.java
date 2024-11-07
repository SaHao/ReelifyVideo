package com.reelify.kkkkwillo.net.response;

import com.google.gson.annotations.SerializedName;

public class Response<T> {
    private int code;
    @SerializedName(value = "data")
    private T data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
