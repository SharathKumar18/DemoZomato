package com.sample.zomatodemo.utils;

import java.util.HashMap;
import java.util.Map;

import static com.sample.zomatodemo.utils.ApiConstants.*;

public class NetworkUtils {


    public static String getURL(final String apiCall) {
        String baseURL = ApiConstants.BASE_URL;

        switch (apiCall) {
            case ApiConstants.BASE_URL:
                baseURL = baseURL.concat(ApiConstants.BASE_URL);
                return baseURL;
            case ApiConstants.LOCATION_ID:
                baseURL = baseURL.concat(ApiConstants.LOCATION_ID);
                return baseURL;
            case ApiConstants.SEARCH:
                baseURL = baseURL.concat(ApiConstants.SEARCH);
                return baseURL;
            case ApiConstants.DETAIL:
                baseURL = baseURL.concat(ApiConstants.DETAIL);
                return baseURL;
        }
        return null;
    }

    public static Map<String, String> getCityIdQuery(final String cityName) {
        Map<String, String> queryMap = new HashMap<>();
        queryMap.put(QUERY, cityName);
        queryMap.put(KEY_COUNT, COUNT);
        return queryMap;
    }

    public static Map<String,String> getRestaurantSearchQuery(final String entityId, int offset,final String latitude,
                                                              final String longitude){
        Map<String, String> queryMap = new HashMap<>();
        queryMap.put(ENTITY_ID, entityId);
        queryMap.put(ENTITY_TYPE, CITY);
        queryMap.put(Q,P);
        queryMap.put(LATITUDE, latitude);
        queryMap.put(LONGITUDE, longitude);
        queryMap.put(KEY_COUNT, COUNT);
        queryMap.put(START,""+offset);
        queryMap.put(RADIUS,RADIUS_DEFAULT_VALUE);
        queryMap.put(SORT,SORT_RATING);
        queryMap.put(ORDER,ORDER_ASC);
        return queryMap;
    }

    public static Map<String, String> getRestaurantDetail(final String restaurantID) {
        Map<String, String> queryMap = new HashMap<>();
        queryMap.put(RESID, restaurantID);
        return queryMap;
    }

}
