package com.sample.zomatodemo.ui.home;

import android.arch.lifecycle.ViewModelProviders;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.inputmethod.EditorInfo;

import com.sample.zomatodemo.data.model.UiHelper;
import com.sample.zomatodemo.data.response.searchdata.Restaurant;
import com.sample.zomatodemo.databinding.HomeFragmentBinding;
import com.sample.zomatodemo.recyclercomponents.RestaurantListAdapter;
import com.sample.zomatodemo.ui.base.BaseFragment;
import com.sample.zomatodemo.utils.ApiConstants;
import com.sample.zomatodemo.utils.AppConstants;
import com.sample.zomatodemo.R;
import com.sample.zomatodemo.utils.AppUtils;
import com.sample.zomatodemo.utils.PreferenceHelper;
import com.sample.zomatodemo.utils.RxEvent;

import java.util.List;


public class HomeFragment extends BaseFragment {

    private List<Restaurant> mRestaurantData;
    private RestaurantListAdapter mAdapter;
    private LinearLayoutManager mLayoutManager;
    private int mPreviousTotal;
    private boolean loading = true;

    public static HomeFragment newInstance() {
        return new HomeFragment();
    }

    @Override
    protected int getFragmentLayoutId() {
        return R.layout.home_fragment;
    }

    @Override
    protected HomeFragmentBinding getDataBinder() {
        return (HomeFragmentBinding) super.getDataBinder();
    }

    @Override
    protected void initViews(View view) {
        getDataBinder().setHomeViewModel(getViewModel());
        getViewModel().init();
        showCurrentCity();
        addDataObserver();
        addSearchListener();
    }

    private void addSearchListener() {
        getDataBinder().search.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                getViewModel().setErrorTextValue(false);
                AppUtils.closeSoftKeyboard(getDataBinder().search);
                String searchedCity = getDataBinder().search.getText().toString().trim();
                if (!searchedCity.equalsIgnoreCase(getViewModel().getCityName())) {
                    if (mRestaurantData != null) {
                        mRestaurantData.clear();
                    }
                    resetPreviousSearchResult();
                    mPreviousTotal = 0;
                    fetchRestaurantList(searchedCity, 0);
                }
                return true;
            }
            return false;
        });
    }

    private void addDataObserver() {
        getViewModel().getUiLiveData().observe(this, this::handleUICallbacks);
        getViewModel().getRestaurantList().observe(this, restaurants -> {
            if (restaurants != null) {
                loading = false;
                mRestaurantData = restaurants;
                getDataBinder().restaurantList.setVisibility(View.VISIBLE);
                getViewModel().setErrorTextValue(false);
                updateListData();
                mPreviousTotal = restaurants.size();
            }
        });
        fetchRestaurantList(PreferenceHelper.getInstance().getPrefString(AppConstants.KEY_CITY_NAME), 0);
    }

    private void updateListData() {
        updateCityNameForResult();
        if (mAdapter == null) {
            setUpRecyclerView();
        } else {
            mAdapter.updateItems(mRestaurantData, mPreviousTotal);
        }
    }

    private void updateCityNameForResult() {
        if (mRestaurantData != null) {
            for (Restaurant restaurant : mRestaurantData) {
                restaurant.getRestaurant().setCityName(getViewModel().getCityName());
            }
        }
    }

    private RecyclerView.OnScrollListener recyclerViewOnScrollListener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
        }

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            if (mLayoutManager != null) {
                if (dy > 0) {
                    int visibleItemCount = mLayoutManager.getChildCount();
                    int totalItemCount = mLayoutManager.getItemCount();
                    int previousVisibleItems = mLayoutManager.findFirstVisibleItemPosition();
                    if (!loading) {
                        if ((visibleItemCount + previousVisibleItems) >= totalItemCount
                                && mPreviousTotal >= ApiConstants.DEFAULT_COUNT) {
                                loading = true;
                                fetchRestaurantList(getViewModel().getCityName(), getViewModel().getItemCount());
                        }
                    }
                }
            }
        }
    };


    private void setUpRecyclerView() {
        mLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        getDataBinder().restaurantList.setLayoutManager(mLayoutManager);
        mAdapter = new RestaurantListAdapter(mRestaurantData);
        getDataBinder().restaurantList.setAdapter(mAdapter);
        getDataBinder().restaurantList.addItemDecoration(new DividerItemDecoration(getDataBinder().
                restaurantList.getContext(), DividerItemDecoration.VERTICAL));
        getDataBinder().restaurantList.addOnScrollListener(recyclerViewOnScrollListener);

        mAdapter.setItemSelectedListener(restaurantInfo -> {
            RxEvent detailEvent = new RxEvent(RxEvent.EVENT_SHOW_DETAIL_SCREEN);
            restaurantInfo.setCityName(getViewModel().getCityName());
            detailEvent.setData(restaurantInfo);
            mRxBus.send(detailEvent);
        });
    }

    private void fetchRestaurantList(String cityName, int offset) {
        getViewModel().getRestaurants(cityName, offset);
    }

    private void showCurrentCity() {
        if (getViewModel() != null)
            getViewModel().setCityName(PreferenceHelper.getInstance().getPrefString(AppConstants.KEY_CITY_NAME));
        StringBuilder builder = new StringBuilder();
        builder.append(getString(R.string.current_city))
                .append(PreferenceHelper.getInstance().getPrefString(AppConstants.KEY_CITY_NAME));
        getDataBinder().currentCity.setText(builder);
    }

    private void handleUICallbacks(UiHelper uiHelper) {
        switch (uiHelper.getStatus()) {
            case AppConstants.UIConstants.SHOW_PROGRESS:
                getDataBinder().progressCircular.setVisibility(View.VISIBLE);
                break;
            case AppConstants.UIConstants.HIDE_PROGRESS:
                getDataBinder().progressCircular.setVisibility(View.GONE);
                break;
            case AppConstants.UIConstants.RESTUARANT_NOT_FOUND:
                resetPreviousSearchResult();
                getDataBinder().restaurantList.setVisibility(View.GONE);
                getDataBinder().errorMessage.setVisibility(View.VISIBLE);
                getViewModel().setErrorTextValue(true);
                getDataBinder().errorMessage.setText(getString(R.string.no_restaurant_found));
                break;
            case AppConstants.UIConstants.RESTAURANT_FETCH_FAILED:
                resetPreviousSearchResult();
                getDataBinder().restaurantList.setVisibility(View.GONE);
                getDataBinder().errorMessage.setVisibility(View.VISIBLE);
                getViewModel().setErrorTextValue(true);
                getDataBinder().errorMessage.setText(getString(R.string.something_went_wrong_please_try_again));
                break;
        }
    }

    private void resetPreviousSearchResult() {
        if (mAdapter != null) {
            mAdapter.clearData();
        }
    }

    public HomeViewModel getViewModel() {
        return ViewModelProviders.of(this).get(HomeViewModel.class);
    }

    @Override
    protected void resumeScreen() {

    }

    @Override
    protected int handleBusCallback(Object event) {
        if (event instanceof RxEvent) {
            switch (((RxEvent) event).getEventTag()) {
                case RxEvent.EVENT_LOCATION_UPDATED:
                    if (mRestaurantData == null) {
                        showCurrentCity();
                        fetchRestaurantList(PreferenceHelper.getInstance().getPrefString(AppConstants.KEY_CITY_NAME), 0);
                    }
                    break;
            }
        }

        return 0;
    }
}
