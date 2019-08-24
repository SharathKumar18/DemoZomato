package com.sample.zomatodemo;

import com.sample.zomatodemo.data.response.restaurantid.RestaurantIdClass;
import com.sample.zomatodemo.network.DataManager;
import com.sample.zomatodemo.network.RetrofitInterface;
import com.sample.zomatodemo.ui.home.HomeViewModel;
import com.sample.zomatodemo.utils.ApiConstants;
import com.sample.zomatodemo.utils.NetworkUtils;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import io.reactivex.Observable;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.when;

@RunWith(JUnit4.class)
public class HomeViewModelTest {

    @Mock
    private HomeViewModel mViewModel;

    @Mock
    private RetrofitInterface mRetrofitInerface;

    @Mock
    private DataManager mManager;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

    }

    @Test
    public void error_text_false()  {
        assertFalse(mViewModel.getErrorTextValue());
    }

    @Test
    public void city_name_null() {
        assertNull(mViewModel.getCityName());
    }

    @Test
    public void fetch_restaurant_data() {
        RestaurantIdClass charactersResponseModel = new RestaurantIdClass();

        when(mRetrofitInerface.getCityId(NetworkUtils.getURL(ApiConstants.LOCATION_ID),
                NetworkUtils.getCityIdQuery("Bengaluru")))
                .thenReturn(Observable.just(charactersResponseModel));
    }


}
