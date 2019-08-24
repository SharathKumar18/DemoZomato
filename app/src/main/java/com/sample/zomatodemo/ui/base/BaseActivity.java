package com.sample.zomatodemo.ui.base;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.sample.zomatodemo.R;
import com.sample.zomatodemo.application.ZomatoDemoApplication;
import com.sample.zomatodemo.rxbus.MainBus;
import com.sample.zomatodemo.utils.AppUtils;

import javax.inject.Inject;

import io.reactivex.observers.DisposableObserver;

public abstract class BaseActivity extends AppCompatActivity {

    protected abstract int getLayoutId();

    public abstract int getContainerId();

    abstract protected void initViews();

    protected abstract int handleBusCallback(Object event);

    @Inject
    protected MainBus mRxBus;
    private boolean mShowNetworkRestore;
    private BroadcastReceiver mReceiver;
    private DisposableObserver<Object> mDisposable;
    private ViewDataBinding mBinding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, getLayoutId());
        ((ZomatoDemoApplication)getApplication()).getApplicationComponent().inject(this);
        getSupportFragmentManager().addOnBackStackChangedListener(getBackStackListener());
        addNetworkChangeListener();
        registerForBusCallback();
        initViews();
    }

    private void addNetworkChangeListener() {
        mReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
               updateNetworkStatusChange(AppUtils.isNetworkConnected());

            }
        };
    }
    public ViewDataBinding getDataBinder(){
        return mBinding;
    }

    private void updateNetworkStatusChange(boolean isConnected) {
        if(!isConnected){
            mShowNetworkRestore =true;
            showSnackBarMessage(getString(R.string.disconnected));
        }else {
            if(mShowNetworkRestore){
                showSnackBarMessage(getString(R.string.connected));
            }
        }

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
    protected void onResume() {
        super.onResume();
        registerReceiver(mReceiver, new IntentFilter(
                ConnectivityManager.CONNECTIVITY_ACTION));
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(mReceiver);
    }

    @Override
    protected void onDestroy() {
        unSubScribe();
        super.onDestroy();
    }

    protected void showSnackBarMessage(String message) {
        View parentLayout = findViewById(android.R.id.content);
        if (parentLayout != null) {
            Snackbar snackbar = Snackbar.make(parentLayout, message, Snackbar.LENGTH_LONG);
            snackbar.show();
        }
    }

    private FragmentManager.OnBackStackChangedListener getBackStackListener() {
        FragmentManager.OnBackStackChangedListener result = new FragmentManager.OnBackStackChangedListener() {
            public void onBackStackChanged() {
                FragmentManager manager = getSupportFragmentManager();
                if (manager != null) {
                    BaseFragment fragment = (BaseFragment) manager.findFragmentById(getContainerId());
                    if (fragment != null) {
                        fragment.resumeScreen();
                    }
                }
            }
        };
        return result;
    }

}