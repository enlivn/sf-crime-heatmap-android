package com.example.android.sfcrimeheatmap.activities;

import android.support.v7.app.AppCompatActivity;

import com.afollestad.materialdialogs.MaterialDialog;

public interface BaseActivityCallbacks {
    AppCompatActivity getBaseActivityContext();

    void showProgressDialog();

    void dismissProgressDialog();

    void showMaterialDialog(int resId);

    void showMaterialDialog(int resId, MaterialDialog.ButtonCallback callback);
}
