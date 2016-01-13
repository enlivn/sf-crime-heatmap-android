package com.example.android.sfcrimeheatmap.activities.heatmap;

import com.example.android.sfcrimeheatmap.activities.BaseActivityModule;
import com.example.android.sfcrimeheatmap.application.CMAppComponent;
import com.example.android.sfcrimeheatmap.di.annotations.ActivityScope;

import dagger.Component;

@ActivityScope
@Component(modules = {CrimeMapActivityModule.class, BaseActivityModule.class} ,dependencies = CMAppComponent.class)
public interface CrimeMapActivityComponent {
    void inject(CrimeMapActivity activity);
}
