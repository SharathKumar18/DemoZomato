package com.sample.zomatodemo.ui.base;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.MutableLiveData;

import com.sample.zomatodemo.application.ZomatoDemoApplication;
import com.sample.zomatodemo.data.model.UiHelper;
import com.sample.zomatodemo.network.DataManager;
import com.sample.zomatodemo.rxbus.MainBus;
import com.sample.zomatodemo.utils.AppConstants;

import javax.inject.Inject;

import io.reactivex.observers.DisposableObserver;

public abstract class BaseViewModel extends AndroidViewModel {

    private static final String TAG = "VKBaseViewModel";
    @Inject
    protected MainBus mRxBus;
    private DisposableObserver mDisposable;
    @Inject
    public DataManager mDataManager;

    protected abstract int handleBusCallback(Object event);

    private MutableLiveData<UiHelper> mUiLiveData = new MutableLiveData<>();

    public MutableLiveData<UiHelper> getUiLiveData() {
        return mUiLiveData;
    }

    public BaseViewModel(Application application) {
        super(application);

    }
    public void showProgress(){
        UiHelper helper=new UiHelper();
        helper.setStatus(AppConstants.UIConstants.SHOW_PROGRESS);
        mUiLiveData.setValue(helper);
    }

    public void hideProgress(){
        UiHelper helper=new UiHelper();
        helper.setStatus(AppConstants.UIConstants.HIDE_PROGRESS);
        mUiLiveData.setValue(helper);
    }

    public void init(){
        ((ZomatoDemoApplication)getApplication()).getApplicationComponent().inject(this);
        registerForBusCallback();
    }

    public void sendUiData(int status){
        UiHelper helper=new UiHelper();
        helper.setStatus(status);
        mUiLiveData.setValue(helper);
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
}
