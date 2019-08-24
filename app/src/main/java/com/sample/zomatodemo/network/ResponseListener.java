package com.sample.zomatodemo.network;

import io.reactivex.observers.DisposableObserver;

public abstract class ResponseListener <T>  extends DisposableObserver<T> {

    @Override
    public void onComplete() {
    }

    @Override
    public void onError(Throwable e) {
        onFailure(e);
    }

    @Override
    public void onNext(T t) {
        onSuccess(t);
    }

    public abstract void onSuccess(T response);

    public abstract void onFailure(Throwable error);
}
