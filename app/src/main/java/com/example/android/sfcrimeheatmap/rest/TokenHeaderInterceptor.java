package com.example.android.sfcrimeheatmap.rest;

import com.squareup.okhttp.Interceptor;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;

public class TokenHeaderInterceptor implements Interceptor {

    private String apiToken;

    public TokenHeaderInterceptor(String apiToken) {
        this.apiToken = apiToken;
    }

    @Override
    public Response intercept(Interceptor.Chain chain)
            throws IOException {
        Request request = chain.request();
        request = request.newBuilder()
                .addHeader("X-App-Token", apiToken)
                .build();
        return chain.proceed(request);
    }
}