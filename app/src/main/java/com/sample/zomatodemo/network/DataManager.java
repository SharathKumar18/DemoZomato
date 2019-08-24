package com.sample.zomatodemo.network;

import com.sample.zomatodemo.data.response.detail.RestaurantDetail;
import com.sample.zomatodemo.data.response.restaurantid.LocationSuggestion;
import com.sample.zomatodemo.data.response.searchdata.SearchResult;
import com.sample.zomatodemo.data.response.restaurantid.RestaurantIdClass;
import com.sample.zomatodemo.utils.ApiConstants;
import com.sample.zomatodemo.utils.NetworkUtils;

import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

public class DataManager {
    private RetrofitInterface mApiService;

    public DataManager(RetrofitInterface retrofitInterface) {
        mApiService = retrofitInterface;
    }

    public Disposable getRestaurantants(String cityName,int offset,ResponseListener<SearchResult> callback) {
        return mApiService.getCityId(NetworkUtils.getURL(ApiConstants.LOCATION_ID),
                NetworkUtils.getCityIdQuery(cityName))
                .flatMap((Function<RestaurantIdClass, ObservableSource<SearchResult>>) restaurantIdClass -> {
                    if (restaurantIdClass != null
                            && restaurantIdClass.getLocationSuggestions() != null
                            && restaurantIdClass.getLocationSuggestions().size() > 0
                            && restaurantIdClass.getLocationSuggestions().get(0) != null) {
                        LocationSuggestion suggestion= restaurantIdClass.getLocationSuggestions().get(0);
                        if(suggestion!=null){
                            return mApiService.getRestaurants(NetworkUtils.getURL(ApiConstants.SEARCH),
                                    NetworkUtils.getRestaurantSearchQuery(String.valueOf(suggestion.getCityId()),
                                            offset,String.valueOf(suggestion.getLatitude()),String.valueOf(suggestion.getLongitude())));
                        }
                    }
                   return null;
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .onErrorResumeNext(throwable -> null)
                .subscribeWith(callback);
    }

    public Disposable getRestaurantantDetail(String resID,ResponseListener<RestaurantDetail> callback) {
        return mApiService.getRestaurantDetail(NetworkUtils.getURL(ApiConstants.DETAIL),
                NetworkUtils.getRestuarantDetail(resID))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .onErrorResumeNext(throwable -> null)
                .subscribeWith(callback);
    }

}
