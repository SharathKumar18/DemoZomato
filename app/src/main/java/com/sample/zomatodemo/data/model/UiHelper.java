package com.sample.zomatodemo.data.model;

public class UiHelper {
    private int status;
    private String mMessage;

    public UiHelper() {
    }
    public UiHelper(int status) {
        this.status = status;
    }
    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status=status;
    }

    public void setMessage(String message){
        mMessage=message;
    }
    public String getMessage(){
        return mMessage;
    }
}
