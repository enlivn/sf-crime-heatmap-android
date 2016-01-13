package com.example.android.sfcrimeheatmap.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.afollestad.materialdialogs.MaterialDialog;
import com.example.android.sfcrimeheatmap.R;
import com.example.android.sfcrimeheatmap.application.CMApp;
import com.example.android.sfcrimeheatmap.application.CMAppComponent;

import javax.inject.Inject;

public abstract class BaseActivity extends AppCompatActivity implements BaseActivityCallbacks {
    protected abstract void setupDataInjection(CMAppComponent appComponent);

    @Inject
    MaterialDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bootstrapDI();
    }

    private void bootstrapDI() {
        CMApp app = (CMApp) getApplication();
        setupDataInjection(app.getAppComponent());
    }

    @Override
    public AppCompatActivity getBaseActivityContext(){
        return this;
    }

    @Override
    public void showProgressDialog(){
        if(dialog != null && !dialog.isShowing()) dialog.show();
    }

    @Override
    public void dismissProgressDialog(){
        if(dialog != null && dialog.isShowing()) dialog.dismiss();
    }


    @Override
    public void showMaterialDialog(int resId){
        showMaterialDialog(resId, new MaterialDialog.ButtonCallback() {
            @Override
            public void onPositive(MaterialDialog dialog) {
                super.onPositive(dialog);
                finish();
            }
        });
    }

    @Override
    public void showMaterialDialog(int resId, MaterialDialog.ButtonCallback callback){
        new MaterialDialog.Builder(this)
                .cancelable(false)
                .title(resId)
                .positiveText(R.string.okay)
                .callback(callback)
                .build()
                .show();
    }
}
