package com.example.android.sfcrimeheatmap.application;

import android.app.Application;

import com.example.android.sfcrimeheatmap.rest.API;
import com.example.android.sfcrimeheatmap.rest.RestModule;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {CMModule.class, RestModule.class})
public interface CMAppComponent {
    API api();

    final class Initializer {
        public static CMAppComponent initialize(Application app){
            return DaggerCMAppComponent
                    .builder()
                    .cMModule(new CMModule(app))
                    .restModule(new RestModule())
                    .build();
        }
    }
}
