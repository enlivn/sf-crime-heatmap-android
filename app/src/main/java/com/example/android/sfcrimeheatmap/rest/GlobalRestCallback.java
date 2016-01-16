package com.example.android.sfcrimeheatmap.rest;

import com.example.android.sfcrimeheatmap.R;
import com.example.android.sfcrimeheatmap.activities.BaseActivityCallbacks;

import retrofit.Callback;

public abstract class GlobalRestCallback<T> implements Callback<T> {

    private BaseActivityCallbacks baseActivity;

    public GlobalRestCallback(BaseActivityCallbacks baseActivity) {
        this.baseActivity = baseActivity;
    }

    @Override
    public void onFailure(Throwable t) {
        baseActivity.dismissProgressDialog();
        if (t instanceof NoNetworkException) {
            baseActivity.showMaterialDialog(R.string.no_network_available);
        }
    }
}
