package com.sample.zomatodemo.network;

import io.reactivex.observers.DisposableObserver;

public abstract class ResponseListener <T>  extends DisposableObserver<T> {

    @Override
    public void onComplete() {
    }

    @Override
    public void onError(Throwable e) {
        onFailure();
    }

    @Override
    public void onNext(T t) {
        onSuccess(t);
    }

    protected abstract void onSuccess(T response);

    protected abstract void onFailure();
}
