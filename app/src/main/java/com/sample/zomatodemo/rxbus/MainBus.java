package com.sample.zomatodemo.rxbus;

import io.reactivex.Observable;

public interface MainBus {
    void send(final Object event);

    Observable<Object> toObservable();

    boolean hasObservers();
}


