package com.sample.zomatodemo.ui.base;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sample.zomatodemo.application.ZomatoDemoApplication;
import com.sample.zomatodemo.rxbus.MainBus;

import javax.inject.Inject;

import io.reactivex.observers.DisposableObserver;

public abstract class BaseFragment extends Fragment {

    abstract protected int getFragmentLayoutId();

    protected abstract void initViews(View view);

    protected abstract void resumeScreen();

    protected abstract int handleBusCallback(Object event);

    private ViewDataBinding mBinder;
    @Inject
    protected MainBus mRxBus;
    private DisposableObserver<Object> mDisposable;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    protected View inflateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinder = DataBindingUtil.inflate(inflater, getFragmentLayoutId(), container, false);
        return mBinder.getRoot();
    }

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflateView(inflater, container, savedInstanceState);
        setRetainInstance(true);
        ((ZomatoDemoApplication)getActivity().getApplication()).getApplicationComponent().inject(this);
        registerForBusCallback();
        initViews(view);
        return view;
    }

    protected ViewDataBinding getDataBinder() {
        return mBinder;
    }

    public void registerForBusCallback() {
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

    public void unSubScribe() {
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

    public void showSnackBarMessage(String message) {
        if (getView() != null) {
            Snackbar snackbar = Snackbar.make(getView(), message, Snackbar.LENGTH_LONG);
            snackbar.show();
        }
    }
}
