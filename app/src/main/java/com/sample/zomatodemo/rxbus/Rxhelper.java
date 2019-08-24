package com.sample.zomatodemo.rxbus;

import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;

public class Rxhelper implements MainBus {

    private static Rxhelper mBusClass;
    final PublishSubject<Object> mBus;

    public static Rxhelper getRxBus() {
        if (mBusClass == null) {
            mBusClass = new Rxhelper();
        }
        return mBusClass;
    }

    public Rxhelper() {
        mBus = PublishSubject.create();
    }

    @Override
    public void send(Object event) {
        mBus.onNext(event);
    }

    @Override
    public Observable<Object> toObservable() {
        return mBus;
    }

    @Override
    public boolean hasObservers() {
        return mBus.hasObservers();
    }

}
