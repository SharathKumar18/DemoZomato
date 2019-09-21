package com.sample.zomatodemo.utils;

public class RxEvent <T> {
    public static final int EVENT_LOAD_HOME = 1;
    public static final int EVENT_LOCATION_UPDATED = 2;
    public static final int EVENT_SHOW_DETAIL_SCREEN = 3;

    private int mEventTag;

    private T mData;

    public RxEvent(int mEventTag) {
        this.mEventTag = mEventTag;
    }

    public int getEventTag() {
        return mEventTag;
    }

    public T getData() {
        return mData;
    }

    public void setData(T mData) {
        this.mData = mData;
    }
}
