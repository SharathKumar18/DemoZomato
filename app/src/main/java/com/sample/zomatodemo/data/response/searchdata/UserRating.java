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
    @SerializedName("rating_text")
    @Expose
    private String ratingText;
    @SerializedName("rating_color")
    @Expose
    private String ratingColor;
    @SerializedName("votes")
    @Expose
    private Integer votes;

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

    public String getRatingText() {
        return ratingText;
    }

    public void setRatingText(String ratingText) {
        this.ratingText = ratingText;
    }

    public String getRatingColor() {
        return ratingColor;
    }

    public void setRatingColor(String ratingColor) {
        this.ratingColor = ratingColor;
    }

    public Integer getVotes() {
        return votes;
    }

    public void setVotes(Integer votes) {
        this.votes = votes;
    }
}
