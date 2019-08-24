package com.sample.zomatodemo.ui.home;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.view.View;

import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.tasks.Task;
import com.sample.zomatodemo.R;
import com.sample.zomatodemo.databinding.ActivityHomeBinding;
import com.sample.zomatodemo.ui.base.BaseActivity;
import com.sample.zomatodemo.ui.detail.DetailFragment;
import com.sample.zomatodemo.ui.splash.SplashFragment;
import com.sample.zomatodemo.utils.FragmentNavigator;
import com.sample.zomatodemo.utils.LocationAccessHelper;
import com.sample.zomatodemo.utils.PreferenceHelper;
import com.sample.zomatodemo.utils.RxEvent;

import static com.sample.zomatodemo.utils.AppConstants.GET_LOCATION;
import static com.sample.zomatodemo.utils.AppConstants.KEY_CITY_NAME;
import static com.sample.zomatodemo.utils.AppConstants.KEY_LATTITUDE;
import static com.sample.zomatodemo.utils.AppConstants.KEY_LONGITUDE;

public class HomeActivity extends BaseActivity {

    private static final long LOCATION_REFRESH_TIME = 5000;
    private static final long LOCATION_REFRESH_DISTANCE = 1000;
    private static final int LOCATION_SETTINGS_REQUEST = 1;
    private ActivityHomeBinding mBinder;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_home;
    }

    @Override
    public int getContainerId() {
        return R.id.home_container;
    }

    @Override
    protected void initViews() {
        loadSplashFragment();
        mBinder = (ActivityHomeBinding) getDataBinder();
        boolean isPermissionGranted = LocationAccessHelper.checkLocationPermission(this);
        if (isPermissionGranted) {
            findUserLocation();
        }
    }

    private final LocationListener mLocationListener = new LocationListener() {
        @Override
        public void onLocationChanged(final Location location) {
            saveLocation(location);
        }

        @Override
        public void onStatusChanged(String s, int i, Bundle bundle) {

        }

        @Override
        public void onProviderEnabled(String s) {

        }

        @Override
        public void onProviderDisabled(String s) {

        }
    };

    private void saveLocation(Location location) {
        String cityName = LocationAccessHelper.getCity(HomeActivity.this, location.getLatitude(), location.getLongitude());
        PreferenceHelper.getInstance().editPrefString(KEY_CITY_NAME, cityName);
        PreferenceHelper.getInstance().editPrefLong(KEY_LATTITUDE, (float) location.getLatitude());
        PreferenceHelper.getInstance().editPrefLong(KEY_LONGITUDE, (float) location.getLongitude());
        RxEvent event = new RxEvent(RxEvent.EVENT_LOCATION_UPDATED);
        mRxBus.send(event);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case GET_LOCATION: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (LocationAccessHelper.checkSelfPermission(this)) {
                        requestLocation();
                    }
                }
            }
        }
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == LOCATION_SETTINGS_REQUEST) {
            switch (resultCode) {
                case Activity.RESULT_OK:
                    findUserLocation();
                    break;
            }
        }
    }

    private void requestLocation() {
        LocationRequest mLocationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(10 * 1000)
                .setFastestInterval(1000);

        LocationSettingsRequest.Builder settingsBuilder = new LocationSettingsRequest.Builder()
                .addLocationRequest(mLocationRequest);
        settingsBuilder.setAlwaysShow(true);

        SettingsClient settingsClient = LocationServices.getSettingsClient(this);
        Task<LocationSettingsResponse> result = settingsClient.checkLocationSettings(settingsBuilder.build());
        result.addOnSuccessListener(locationSettingsResponse -> findUserLocation())
                .addOnFailureListener(e -> {
                    int statusCode = ((ApiException) e).getStatusCode();
                    switch (statusCode) {
                        case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                            try {
                                ResolvableApiException exception = (ResolvableApiException) e;
                                exception.startResolutionForResult(HomeActivity.this,LOCATION_SETTINGS_REQUEST);
                            } catch (IntentSender.SendIntentException sie) {
                            }
                            break;
                    }
                });
    }

    private void findUserLocation() {
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        assert locationManager != null;
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, LOCATION_REFRESH_TIME,
                LOCATION_REFRESH_DISTANCE, mLocationListener);
    }

    private void loadSplashFragment() {
        FragmentNavigator.replaceFragment(this, getSupportFragmentManager(),
                getContainerId(), SplashFragment.newInstance(), null, false,
                SplashFragment.class.getSimpleName());
    }

    public void loadHomeFragment() {
        FragmentNavigator.replaceFragment(this, getSupportFragmentManager(),
                getContainerId(), HomeFragment.newInstance(), null, false,
                HomeFragment.class.getSimpleName());
        mBinder.toolbar.toolbar.setVisibility(View.VISIBLE);
    }

    public void loadDetailFragment(Object object) {
        FragmentNavigator.addFragment(this, getSupportFragmentManager(),
                getContainerId(), DetailFragment.newInstance(object), null, false,
                HomeFragment.class.getSimpleName());
        mBinder.toolbar.toolbar.setVisibility(View.VISIBLE);
    }


    @Override
    protected int handleBusCallback(Object event) {
        if (event instanceof RxEvent) {
            switch (((RxEvent) event).getEventTag()) {
                case RxEvent.EVENT_LOAD_HOME:
                    loadHomeFragment();
                    break;
                case RxEvent.EVENT_SHOW_DETAIL_SCREEN:
                    loadDetailFragment(((RxEvent) event).getData());
                    break;
            }
        }
        return 0;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
}
