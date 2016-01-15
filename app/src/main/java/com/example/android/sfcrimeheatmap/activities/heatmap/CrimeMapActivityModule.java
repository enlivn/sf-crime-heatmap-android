package com.example.android.sfcrimeheatmap.activities.heatmap;

import com.example.android.sfcrimeheatmap.di.annotations.ActivityScope;
import com.example.android.sfcrimeheatmap.models.heatmap.DistrictModel;
import com.example.android.sfcrimeheatmap.rest.API;

import java.util.ArrayList;
import java.util.List;

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

    @Provides
    @ActivityScope
    List<DistrictModel> provideDistricts(){
        List<DistrictModel> districtModels = new ArrayList<>();
        for(com.example.android.sfcrimeheatmap.models.heatmap.enums.District districtName: com.example.android.sfcrimeheatmap.models.heatmap.enums.District.values()){
            DistrictModel districtModel = new DistrictModel(districtName);
            districtModels.add(districtModel);
        }

        return districtModels;
    }
}
