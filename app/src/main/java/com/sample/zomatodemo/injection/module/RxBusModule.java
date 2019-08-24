package com.sample.zomatodemo.injection.module;

import com.sample.zomatodemo.rxbus.MainBus;
import com.sample.zomatodemo.rxbus.Rxhelper;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class RxBusModule {

    @Provides
    @Singleton
    MainBus getRxhelper() {
        return Rxhelper.getRxBus();
    }

}
