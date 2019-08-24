package com.sample.zomatodemo.recyclercomponents;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.sample.zomatodemo.R;
import com.sample.zomatodemo.data.response.searchdata.Restaurant;
import com.sample.zomatodemo.ui.base.BaseViewHolder;

import java.util.List;

public class RestaurantListAdapter extends RecyclerView.Adapter<BaseViewHolder> {

    private List<Restaurant> mRestaurantList;
    private BaseViewHolder.OnItemSelectListener mListener;

    public RestaurantListAdapter(List<Restaurant> restaurantList) {
        mRestaurantList = restaurantList;
    }

    public void setItemSelectedListener(BaseViewHolder.OnItemSelectListener listener) {
        mListener = listener;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewDataBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                R.layout.search_item, parent, false);
        return new RestaurantItemViewHolder(binding);
    }

    public void updateItems(List<Restaurant> restaurantList, int previousTotal) {
        mRestaurantList = restaurantList;
        if (mRestaurantList != null) {
            notifyItemRangeChanged(previousTotal, mRestaurantList.size());
        } else {
            notifyDataSetChanged();
        }
    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, int position) {
        holder.bind(mRestaurantList.get(position).getRestaurant(), mListener, position);
    }

    @Override
    public int getItemCount() {
        return (mRestaurantList == null) ? 0 : mRestaurantList.size();
    }

    public void clearData() {
        if (mRestaurantList != null)
            mRestaurantList.clear();
        notifyDataSetChanged();
    }
}
