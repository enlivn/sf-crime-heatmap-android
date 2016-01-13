package com.example.android.sfcrimeheatmap.activities;

import android.support.v4.app.FragmentActivity;

import com.afollestad.materialdialogs.MaterialDialog;

public interface BaseActivityCallbacks {
    FragmentActivity getBaseActivityContext();
    void showProgressDialog();
    void dismissProgressDialog();
    void showMaterialDialog(int resId);
    void showMaterialDialog(int resId, MaterialDialog.ButtonCallback callback);
}
