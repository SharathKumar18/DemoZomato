package com.sample.zomatodemo.data.response.searchdata;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.sample.zomatodemo.BR;

public class UserRating extends BaseObservable {
    @SerializedName("aggregate_rating")
    @Expose
    private Float aggregateRating;

    private String rating;

    @Bindable
    public Float getAggregateRating() {
        return aggregateRating;
    }

    @Bindable
    public String getRating(){
        return rating;
    }

    public void setRating(String rating) {
        this.rating=rating;
        notifyPropertyChanged(BR.rating);
    }

    public void setAggregateRating(Float aggregateRating) {
        this.aggregateRating = aggregateRating;
        notifyPropertyChanged(BR.aggregateRating);
    }

}
