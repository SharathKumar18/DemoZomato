package com.sample.zomatodemo.data.response.restaurantid;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.sample.zomatodemo.data.response.BaseResponse;

public class LocationSuggestion extends BaseResponse {

    @SerializedName("latitude")
    @Expose
    private Double latitude;
    @SerializedName("longitude")
    @Expose
    private Double longitude;
    @SerializedName("city_id")
    @Expose
    private Integer cityId;

    public Double getLatitude() {
        return latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public Integer getCityId() {
        return cityId;
    }

}
