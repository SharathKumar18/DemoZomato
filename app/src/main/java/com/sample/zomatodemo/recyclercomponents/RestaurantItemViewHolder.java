package com.sample.zomatodemo.recyclercomponents;

import android.databinding.ViewDataBinding;
import android.view.View;

import com.sample.zomatodemo.R;
import com.sample.zomatodemo.data.response.searchdata.RestaurantInfo;
import com.sample.zomatodemo.databinding.SearchItemBinding;
import com.sample.zomatodemo.ui.base.BaseViewHolder;

public class RestaurantItemViewHolder extends BaseViewHolder implements View.OnClickListener {

    private final SearchItemBinding mItemBinding;
    private RestaurantInfo mRestaurant;

    public RestaurantItemViewHolder(ViewDataBinding binding) {
        super(binding);
        mItemBinding = (SearchItemBinding) binding;
    }

    @Override
    protected void handleBusCallback() {
    }

    @Override
    public <T> void bind(T obj, OnItemSelectListener listener, int actualPosition) {
        super.bind(obj, listener, actualPosition);
        if (obj instanceof RestaurantInfo) {
            mRestaurant = (RestaurantInfo) obj;
            mListener = listener;
            mItemBinding.setRestaurant(mRestaurant);
            if (mRestaurant != null && mRestaurant.getUserRating() != null) {
                mItemBinding.restaurantRating.setText(String.valueOf(mRestaurant.getUserRating().getAggregateRating()));
            }
            mItemBinding.parentLayout.setOnClickListener(this);
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == mItemBinding.parentLayout.getId()) {
            if (mListener != null) {
                mListener.onItemSelected(mRestaurant);
            }
        }
    }

}
