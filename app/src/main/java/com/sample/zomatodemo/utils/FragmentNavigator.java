package com.sample.zomatodemo.utils;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;

public class FragmentNavigator {

    public static void addFragment(Activity activity, FragmentManager fm,
                                   @IdRes int containerId, Fragment fragment, Bundle args,
                                   boolean addToBackStack, String tag) {
        if (activity.isFinishing() && fm == null) return;
        try {
            if (args != null) {
                fragment.setArguments(args);
            }
            FragmentTransaction fTransaction = fm.beginTransaction();
            fTransaction.add(containerId, fragment, tag);
            fTransaction.addToBackStack(tag).commitAllowingStateLoss();
            fm.executePendingTransactions();
        } catch (Exception e) {
            Log.e("exceptionTransaction:", e.toString());
        }
    }

    public static void replaceFragment(Activity activity,FragmentManager fm,
                                       @IdRes int containerId, Fragment fragment, Bundle args,
                                       boolean addToBackStack, String tag) {
        if (activity.isFinishing() && fm == null) return;
        try {
            if (args != null) {
                fragment.setArguments(args);
            }
            FragmentTransaction fTransaction = fm.beginTransaction();
            if (addToBackStack) {
                fTransaction.replace(containerId, fragment, tag);
                fTransaction.addToBackStack(tag).commitAllowingStateLoss();
                fm.executePendingTransactions();
            } else {
                fTransaction.replace(containerId, fragment, tag);
                fTransaction.commitAllowingStateLoss();
            }
        } catch (Exception e) {
            Log.e("exceptionTransaction:", e.toString());
        }
    }

}
