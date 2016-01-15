package com.example.android.sfcrimeheatmap.rest;

import android.content.Context;

import com.example.android.sfcrimeheatmap.R;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.logging.HttpLoggingInterceptor;

import java.util.concurrent.TimeUnit;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import retrofit.GsonConverterFactory;
import retrofit.Retrofit;
import retrofit.RxJavaCallAdapterFactory;

@Module
public final class RestModule {

    private static final int TIMEOUT_IN_SECONDS = 5;

    @Provides
    @Singleton
    HttpLoggingInterceptor provideLoggingInterceptor(){
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        return interceptor;
    }

    @Provides
    @Singleton
    TokenHeaderInterceptor provideTokenHeaderInterceptor(Context context){
        return new TokenHeaderInterceptor(context.getString(R.string.api_token));
    }

    @Provides
    @Singleton
    OkHttpClient provideHttpClient(HttpLoggingInterceptor loggingInterceptor, TokenHeaderInterceptor tokenHeaderInterceptor){
        OkHttpClient client = new OkHttpClient();
        client.setReadTimeout(TIMEOUT_IN_SECONDS, TimeUnit.SECONDS);
        client.setConnectTimeout(TIMEOUT_IN_SECONDS, TimeUnit.SECONDS);
        client.interceptors().add(loggingInterceptor);
        client.interceptors().add(tokenHeaderInterceptor);

        return client;
    }

    @Provides
    @Singleton
    ConnectionAwareClient provideConnectionAwareClient(Context context, OkHttpClient client){
        return new ConnectionAwareClient(context, client);
    }

    @Provides
    @Singleton
    Retrofit provideRetrofit(Context context, ConnectionAwareClient client){
        return new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .baseUrl(context.getString(R.string.api_endpoint_base))
                .client(client)
                .build();
    }

    @Provides
    @Singleton
    API provideAPI(Retrofit retrofit){
        return retrofit.create(API.class);
    }
}