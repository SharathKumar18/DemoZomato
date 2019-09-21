package com.sample.zomatodemo.ui.base;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sample.zomatodemo.application.ZomatoDemoApplication;
import com.sample.zomatodemo.rxbus.MainBus;

import java.util.Objects;

import javax.inject.Inject;

import io.reactivex.observers.DisposableObserver;

public abstract class BaseFragment extends Fragment {

    abstract protected int getFragmentLayoutId();

    protected abstract void initViews();

    protected abstract void resumeScreen();

    protected abstract void handleBusCallback(Object event);

    private ViewDataBinding mBinder;
    @Inject
    protected MainBus mRxBus;
    private DisposableObserver<Object> mDisposable;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    private View inflateView(LayoutInflater inflater, @Nullable ViewGroup container) {
        mBinder = DataBindingUtil.inflate(inflater, getFragmentLayoutId(), container, false);
        return mBinder.getRoot();
    }

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflateView(inflater, container);
        setRetainInstance(true);
        ((ZomatoDemoApplication) Objects.requireNonNull(getActivity()).getApplication()).getApplicationComponent().inject(this);
        registerForBusCallback();
        initViews();
        return view;
    }

    protected ViewDataBinding getDataBinder() {
        return mBinder;
    }

    private void registerForBusCallback() {
        if (mRxBus != null) {
            mDisposable = new DisposableObserver<Object>() {
                @Override
                public void onNext(Object event) {
                    handleBusCallback(event);
                }

                @Override
                public void onError(Throwable e) {
                }

                @Override
                public void onComplete() {
                }
            };
            mRxBus.toObservable().share().subscribeWith(mDisposable);
        }
    }

    private void unSubScribe() {
        if (mDisposable != null && !mDisposable.isDisposed()) {
            mDisposable.dispose();
            mDisposable = null;
        }
    }

    @Override
    public void onDestroyView() {
        unSubScribe();
        super.onDestroyView();
    }

}
