package com.sample.zomatodemo.data.response.searchdata;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.sample.zomatodemo.data.response.BaseResponse;

import java.util.List;

public class SearchResult extends BaseResponse {
    @SerializedName("restaurants")
    @Expose
    private List<Restaurant> restaurants = null;

    public List<Restaurant> getRestaurants() {
        return restaurants;
    }

}
