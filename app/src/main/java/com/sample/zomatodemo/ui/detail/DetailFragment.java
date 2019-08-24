package com.sample.zomatodemo.ui.detail;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.sample.zomatodemo.R;
import com.sample.zomatodemo.data.model.UiHelper;
import com.sample.zomatodemo.data.response.detail.RestaurantDetail;
import com.sample.zomatodemo.data.response.searchdata.RestaurantInfo;
import com.sample.zomatodemo.databinding.DetailFragmentBinding;
import com.sample.zomatodemo.ui.base.BaseFragment;
import com.sample.zomatodemo.utils.AppConstants;

public class DetailFragment extends BaseFragment {

    private RestaurantInfo mRestaurantInfo;
    private RestaurantDetail mRestaurantDetail;
    private static final String KEY_RESTAURANT_ITEM = "restaurantItem";

    public static DetailFragment newInstance(Object object) {
        Bundle args = new Bundle();
        if (object instanceof RestaurantInfo) {
            args.putParcelable(KEY_RESTAURANT_ITEM, (RestaurantInfo) object);
        }
        DetailFragment fragment = new DetailFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected DetailFragmentBinding getDataBinder() {
        return (DetailFragmentBinding) super.getDataBinder();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mRestaurantInfo = getArguments().getParcelable(KEY_RESTAURANT_ITEM);
        }
    }

    private void handleUICallbacks(UiHelper uiHelper) {
        switch (uiHelper.getStatus()) {
            case AppConstants.UIConstants.SHOW_PROGRESS:
                getDataBinder().progressCircular.setVisibility(View.VISIBLE);
                break;
            case AppConstants.UIConstants.HIDE_PROGRESS:
                getDataBinder().progressCircular.setVisibility(View.GONE);
                break;
        }
    }

    @Override
    protected int getFragmentLayoutId() {
        return R.layout.detail_fragment;
    }

    @Override
    protected void initViews(View view) {
        getViewModel().init();
        getViewModel().getUiLiveData().observe(this, this::handleUICallbacks);
        if (mRestaurantInfo != null && mRestaurantInfo.getR() != null) {
            getViewModel().setCityName(mRestaurantInfo.getCityName());
            getViewModel().getRestaurantDetail(mRestaurantInfo.getR().getResId());
        }
        getViewModel().getDetailLiveData().observe(this, restaurantDetail -> {
            mRestaurantDetail = restaurantDetail;
            if (restaurantDetail != null) {
                getDataBinder().setDetail(restaurantDetail);
            }
        });

        getDataBinder().btnOrder.setOnClickListener(view1 -> {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            String title = getResources().getString(R.string.chooser_title);
            Intent chooser = Intent.createChooser(intent, title);
            intent.setData(Uri.parse(mRestaurantDetail.getDeeplink()));
            if (getActivity()!=null && intent.resolveActivity(getActivity().getPackageManager()) != null) {
                startActivity(chooser);
            } else {
                intent.setData(Uri.parse(mRestaurantDetail.getUrl()));
                startActivity(chooser);
            }
        });
    }

    @Override
    protected void resumeScreen() {

    }

    @Override
    protected int handleBusCallback(Object event) {
        return 0;
    }

    public DetailViewModel getViewModel() {
        return ViewModelProviders.of(this).get(DetailViewModel.class);
    }
}
