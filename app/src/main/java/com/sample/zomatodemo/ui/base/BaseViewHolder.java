package com.sample.zomatodemo.ui.base;

import android.databinding.ViewDataBinding;
import android.support.v7.widget.RecyclerView;

import com.sample.zomatodemo.data.response.searchdata.RestaurantInfo;
import com.sample.zomatodemo.rxbus.MainBus;

import javax.inject.Inject;

import io.reactivex.observers.DisposableObserver;

public abstract class BaseViewHolder extends RecyclerView.ViewHolder{

    private final ViewDataBinding binding;
    @Inject
    MainBus mRxBus;
    protected OnItemSelectListener mListener;
    protected abstract void handleBusCallback();

    protected BaseViewHolder(final ViewDataBinding binding) {
        super(binding.getRoot());
        this.binding = binding;
        registerForBusCallback();

    }

    public <T> void bind(T obj, OnItemSelectListener listener, int actualPosition) {
        binding.executePendingBindings();
    }

    private void registerForBusCallback() {
        if (mRxBus != null) {
            DisposableObserver mDisposable = new DisposableObserver<Object>() {
                @Override
                public void onNext(Object event) {
                    handleBusCallback();
                }

                @Override
                public void onError(Throwable e) {
                }

                @Override
                public void onComplete() {
                }
            };
            mRxBus.toObservable().share().subscribeWith(mDisposable);
        }
    }


    public interface OnItemSelectListener {
        void onItemSelected(RestaurantInfo restaurantInfo);
    }
}
