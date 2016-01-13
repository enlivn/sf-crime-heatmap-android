package com.example.android.sfcrimeheatmap.activities.heatmap;

import com.example.android.sfcrimeheatmap.activities.MaterialDialogModule;
import com.example.android.sfcrimeheatmap.application.CMAppComponent;
import com.example.android.sfcrimeheatmap.di.annotations.ActivityScope;

import dagger.Component;

@ActivityScope
@Component(modules = {CrimeMapActivityModule.class, MaterialDialogModule.class}, dependencies = CMAppComponent.class)
public interface CrimeMapActivityComponent {
    void inject(CrimeMapActivity activity);
}
