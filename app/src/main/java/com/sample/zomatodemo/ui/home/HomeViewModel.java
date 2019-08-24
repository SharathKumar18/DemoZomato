package com.sample.zomatodemo.ui.home;

import android.app.Application;
import android.arch.lifecycle.MutableLiveData;
import android.databinding.ObservableField;

import com.sample.zomatodemo.data.response.searchdata.Restaurant;
import com.sample.zomatodemo.data.response.searchdata.SearchResult;
import com.sample.zomatodemo.network.ResponseListener;
import com.sample.zomatodemo.ui.base.BaseViewModel;
import com.sample.zomatodemo.utils.AppConstants;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

public class HomeViewModel extends BaseViewModel {

    private CompositeDisposable mDisposable = new CompositeDisposable();
    private MutableLiveData<List<Restaurant>> mRestaurantListLiveData = new MutableLiveData<>();
    private List<Restaurant> mRestaurantList = new ArrayList<>();
    private String mCityName;
    private ObservableField<Boolean> mErrorTextValue = new ObservableField<>();

    public boolean getErrorTextValue() {
        return mErrorTextValue.get();
    }

    public void setErrorTextValue(boolean value) {
        mErrorTextValue.set(value);
    }

    public HomeViewModel(Application application) {
        super(application);
        mErrorTextValue.set(false);
    }

    public MutableLiveData<List<Restaurant>> getRestaurantList() {
        return mRestaurantListLiveData;
    }

    public String getCityName() {
        return mCityName;
    }

    public void setCityName(String cityName) {
        mCityName = cityName;
    }

    public void getRestaurants(final String cityName, int offset) {
        showProgress();
        if (mCityName == null) {
            mCityName = cityName;
        } else if (!mCityName.equalsIgnoreCase(cityName)) {
            mCityName = cityName;
            mRestaurantList.clear();
            mRestaurantListLiveData.setValue(null);
        }
        Disposable disposable = mDataManager.getRestaurantants(cityName, offset, new ResponseListener<SearchResult>() {
            @Override
            public void onSuccess(SearchResult response) {
                hideProgress();
                if (response != null) {
                    if (response.getRestaurants() != null && response.getRestaurants().size() == 0) {
                        sendUiData(AppConstants.UIConstants.RESTUARANT_NOT_FOUND);
                    } else {
                        mRestaurantList.addAll(response.getRestaurants());
                        mRestaurantListLiveData.setValue(mRestaurantList);
                    }
                }
            }

            @Override
            public void onFailure(Throwable error) {
                hideProgress();
                mRestaurantList.clear();
                mRestaurantListLiveData.setValue(null);
                sendUiData(AppConstants.UIConstants.RESTAURANT_FETCH_FAILED);
            }
        });
        mDisposable.add(disposable);
    }

    public int getItemCount() {
        return (mRestaurantList != null) ? mRestaurantList.size() : 0;
    }


    @Override
    protected int handleBusCallback(Object event) {
        return 0;
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        mDisposable.clear();
    }
}
