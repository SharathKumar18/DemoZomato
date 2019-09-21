package com.sample.zomatodemo.ui.detail;

import android.app.Application;
import android.arch.lifecycle.MutableLiveData;

import com.sample.zomatodemo.R;
import com.sample.zomatodemo.data.response.detail.RestaurantDetail;
import com.sample.zomatodemo.data.response.searchdata.UserRating;
import com.sample.zomatodemo.network.ResponseListener;
import com.sample.zomatodemo.ui.base.BaseViewModel;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

public class DetailViewModel extends BaseViewModel {

    private final CompositeDisposable mDisposable = new CompositeDisposable();
    private final MutableLiveData<RestaurantDetail> mDetailLiveData = new MutableLiveData<>();
    private String mCityName;

    public DetailViewModel(Application application) {
        super(application);
    }

    MutableLiveData<RestaurantDetail> getDetailLiveData() {
        return mDetailLiveData;
    }
    public void setCityName(String cityName){
        mCityName=cityName;
    }

    void getRestaurantDetail(final int resId) {
        showProgress();
        Disposable disposable = mDataManager.getRestaurantantDetail(String.valueOf(resId),
                new ResponseListener<RestaurantDetail>() {
            @Override
            public void onSuccess(RestaurantDetail response) {
                if(response!=null) {
                    response.setCityName(getApplication().getString(R.string.detail_restaurant_city) +" "+ mCityName);
                    UserRating rating = response.getUserRating();
                    if (rating != null) {
                        String ratingTotal = String.valueOf(rating.getAggregateRating());
                        rating.setRating(getApplication().getString(R.string.detail_restaurant_rating) +" "+ ratingTotal);
                    }
                    hideProgress();
                    mDetailLiveData.setValue(response);
                }
            }

            @Override
            public void onFailure() {
                hideProgress();
                mDetailLiveData.setValue(null);
            }
        });
        mDisposable.add(disposable);
    }

    @Override
    protected void handleBusCallback() {
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        mDisposable.clear();
    }
}
