package com.sample.zomatodemo.data.response.restaurantid;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.sample.zomatodemo.data.response.BaseResponse;

import java.util.List;

public class RestaurantIdClass extends BaseResponse {
    @SerializedName("location_suggestions")
    @Expose
    private List<LocationSuggestion> locationSuggestions = null;

    public List<LocationSuggestion> getLocationSuggestions() {
        return locationSuggestions;
    }

    public void setLocationSuggestions(List<LocationSuggestion> locationSuggestions) {
        this.locationSuggestions = locationSuggestions;
    }
}
