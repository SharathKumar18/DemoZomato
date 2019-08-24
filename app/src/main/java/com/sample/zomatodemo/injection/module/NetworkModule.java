package com.sample.zomatodemo.injection.module;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sample.zomatodemo.network.DataManager;
import com.sample.zomatodemo.network.RetrofitInterface;
import com.sample.zomatodemo.utils.ApiConstants;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
public class NetworkModule {

    @Provides
    @Singleton
    OkHttpClient getOkHttpClient() {
        try {
            OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

            HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

            httpClient.addInterceptor(interceptor);
            httpClient.addInterceptor(chain -> {
                Request original = chain.request();
                Request request = original.newBuilder()
                        .header(ApiConstants.USER_KEY, ApiConstants.API_KEY)
                        .method(original.method(), original.body())
                        .build();
                return chain.proceed(request);
            });
            return httpClient.build();
        } catch (Exception e) {
            return new OkHttpClient().newBuilder().build();
        }
    }

    @Provides
    @Singleton
    Gson getGson() {
        return new GsonBuilder()
                .setLenient()
                .create();
    }

    @Provides
    @Singleton
    Retrofit getRetrofit() {
        return new Retrofit.Builder()
                .baseUrl(ApiConstants.BASE_URL)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(getGson()))
                .client(getOkHttpClient())
                .build();
    }

    @Provides
    @Singleton
    RetrofitInterface getRetrofitInterface() {
        return getRetrofit().create(RetrofitInterface.class);
    }

    @Provides
    @Singleton
    DataManager provideDataManagerClass() {
        return new DataManager(getRetrofitInterface());
    }

}
