package com.sample.zomatodemo.utils;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class LocationAccessHelper {

    private static AlertDialog.Builder mBuilder;

    public static boolean checkLocationPermission(Activity context) {
        if (!checkSelfPermission(context)) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(context,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {
                mBuilder = new AlertDialog.Builder(context)
                        .setTitle(com.sample.zomatodemo.R.string.title_location_permission)
                        .setMessage(com.sample.zomatodemo.R.string.text_location_permission)
                        .setPositiveButton(com.sample.zomatodemo.R.string.ok, (dialogInterface, i) -> {
                            requestLocationPermission(context);
                        });
                mBuilder.create();
                mBuilder.show();
                return false;
            } else {
                requestLocationPermission(context);
                return false;
            }
        } else {
            return true;
        }
    }

    private static void requestLocationPermission(Activity context) {
        ActivityCompat.requestPermissions(context,
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                AppConstants.GET_LOCATION);
    }


    public static boolean checkSelfPermission(Context context) {
        boolean isGranted = ContextCompat.checkSelfPermission(context,
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED;
        if (isGranted) {
            mBuilder = null;
        }
        return isGranted;
    }

    public static String getCity(Activity context, double latitude, double longitude) {
        Geocoder geoCoder = new Geocoder(context, Locale.getDefault());
        try {
            List<Address> address = geoCoder.getFromLocation(latitude, longitude, 1);
            return address.get(0).getLocality();
        } catch (IOException e) {
            return null;
        }
    }
}
