package com.sample.zomatodemo.ui.splash;


import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.View;

import com.sample.zomatodemo.R;
import com.sample.zomatodemo.ui.base.BaseFragment;
import com.sample.zomatodemo.utils.AppConstants;
import com.sample.zomatodemo.utils.RxEvent;

/**
 * A simple {@link Fragment} subclass.
 */
public class SplashFragment extends BaseFragment {

    private Handler mHandler;
    private Runnable mRunnable;

    public static SplashFragment newInstance() {
        Bundle args = new Bundle();
        SplashFragment fragment = new SplashFragment();
        fragment.setArguments(args);
        return fragment;
    }
    public SplashFragment() {
        // Required empty public constructor
    }


    @Override
    protected int getFragmentLayoutId() {
        return R.layout.fragment_splash;
    }

    @Override
    protected void initViews(View view) {
        mHandler = new Handler();
        mRunnable= () -> sendNextScreenEvent();
        mHandler.postDelayed(mRunnable,AppConstants.SPLASH_DELAY);
    }

    private void sendNextScreenEvent() {
        RxEvent event=new RxEvent(RxEvent.EVENT_LOAD_HOME);
        mRxBus.send(event);
    }

    @Override
    protected void resumeScreen() {

    }

    @Override
    protected int handleBusCallback(Object event) {
        return 0;
    }

    @Override
    public void onDestroy() {
        mHandler.removeCallbacks(mRunnable);
        mHandler=null;
        super.onDestroy();
    }
}
