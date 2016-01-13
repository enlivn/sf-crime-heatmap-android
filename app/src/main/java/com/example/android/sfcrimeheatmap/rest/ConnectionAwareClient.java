package com.example.android.sfcrimeheatmap.rest;

import android.content.Context;
import android.net.ConnectivityManager;

import com.squareup.okhttp.Call;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;

public class ConnectionAwareClient extends OkHttpClient {

    private OkHttpClient client;
    private Context context;

    public ConnectionAwareClient(Context context, OkHttpClient client) {
        this.context = context;
        this.client = client;
    }

    @Override
    public Call newCall(Request request) {
        if (isNetworkPresent()) {
            return client.newCall(request);
        } else {
            throw new NoNetworkException();
        }
    }

    private boolean isNetworkPresent(){
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo()!= null && cm.getActiveNetworkInfo().isConnectedOrConnecting();
    }

}