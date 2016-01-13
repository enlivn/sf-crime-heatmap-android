package com.example.android.sfcrimeheatmap.activities;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.afollestad.materialdialogs.MaterialDialog;
import com.example.android.sfcrimeheatmap.R;
import com.example.android.sfcrimeheatmap.application.CMApp;
import com.example.android.sfcrimeheatmap.application.CMAppComponent;

import javax.inject.Inject;

public abstract class BaseActivity extends FragmentActivity implements BaseActivityCallbacks {
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
        DaggerBaseActivityComponent
                .builder()
                .baseActivityModule(new BaseActivityModule(this))
                .build()
                .inject(this);

        setupDataInjection(app.getAppComponent());
    }

    @Override
    public FragmentActivity getBaseActivityContext(){
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
