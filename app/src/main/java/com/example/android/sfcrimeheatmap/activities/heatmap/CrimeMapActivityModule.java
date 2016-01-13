package com.example.android.sfcrimeheatmap.activities.heatmap;

import com.example.android.sfcrimeheatmap.rest.API;
import com.example.android.sfcrimeheatmap.di.annotations.ActivityScope;

import dagger.Module;
import dagger.Provides;

@Module
public class CrimeMapActivityModule {
    private CrimeMapView crimeMapView;

    public CrimeMapActivityModule(CrimeMapView crimeMapView) {
        this.crimeMapView = crimeMapView;
    }

    @Provides
    @ActivityScope
    CrimeMapActivityPresenter providePresenter(API api){
        return new CrimeMapActivityPresenter(crimeMapView, api);
    }
}
