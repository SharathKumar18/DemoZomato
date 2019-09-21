package com.sample.zomatodemo.application;

import android.app.Activity;
import android.app.Application;
import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleObserver;
import android.arch.lifecycle.OnLifecycleEvent;
import android.arch.lifecycle.ProcessLifecycleOwner;
import android.content.Context;

import com.sample.zomatodemo.injection.module.NetworkModule;
import com.sample.zomatodemo.injection.module.RxBusModule;
import com.sample.zomatodemo.injection.component.ApplicationComponent;
import com.sample.zomatodemo.injection.component.DaggerApplicationComponent;

public class ZomatoDemoApplication extends Application implements LifecycleObserver {

    private static ZomatoDemoApplication sInstance;
    private ApplicationComponent mComponent;

    public ZomatoDemoApplication() {
        sInstance = this;
    }

    public static Context getContext() {
        return sInstance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        ProcessLifecycleOwner.get().getLifecycle().addObserver(this);
        initDaggerComponent();
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        sInstance=null;
    }

    private void initDaggerComponent() {
        mComponent = DaggerApplicationComponent.builder()
                .networkModule(new NetworkModule())
                .rxBusModule(new RxBusModule())
                .build();
    }

    public ApplicationComponent getApplicationComponent(){
        return mComponent;
    }
}
