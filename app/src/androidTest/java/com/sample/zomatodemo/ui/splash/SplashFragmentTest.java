package com.sample.zomatodemo.ui.splash;

import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.sample.zomatodemo.R;
import com.sample.zomatodemo.ui.home.HomeActivity;
import com.sample.zomatodemo.utils.AppConstants;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class SplashFragmentTest {

    @Rule
    public ActivityTestRule<HomeActivity> mActivityRule = new ActivityTestRule<>(HomeActivity.class);

    @Before
    public void setUp() {
        mActivityRule.getActivity()
                .getSupportFragmentManager()
                .beginTransaction()
                .add(new SplashFragment(), "splashFragment")
                .commit();
    }

    @Test
    public void checkTitle_splash() {
        onView(withId(R.id.splash_title)).check(matches(withText(R.string.app_name)));
    }


    @Test
    public void checkRedirection_to_home() throws InterruptedException {
        Thread.sleep(AppConstants.SPLASH_DELAY);
        onView(withId(R.id.home_container))
                .check(matches(isDisplayed()));

    }
}

