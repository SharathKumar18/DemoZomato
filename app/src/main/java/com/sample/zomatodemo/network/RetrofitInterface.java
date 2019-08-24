package com.sample.zomatodemo.network;

import com.sample.zomatodemo.data.response.detail.RestaurantDetail;
import com.sample.zomatodemo.data.response.searchdata.SearchResult;
import com.sample.zomatodemo.data.response.restaurantid.RestaurantIdClass;

import java.util.Map;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.QueryMap;
import retrofit2.http.Url;

public interface RetrofitInterface {

    @GET
    Observable<RestaurantIdClass> getCityId(@Url String path, @QueryMap Map<String,String> query);

    @GET
    Observable<SearchResult> getRestaurants(@Url String path, @QueryMap Map<String,String> query);

    @GET
    Observable<RestaurantDetail> getRestaurantDetail(@Url String path, @QueryMap Map<String,String> query);
}
