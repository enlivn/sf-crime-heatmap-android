package com.example.android.sfcrimeheatmap.activities;

import com.example.android.sfcrimeheatmap.di.annotations.ActivityScope;

import dagger.Component;

@Component(modules=BaseActivityModule.class)
@ActivityScope
public interface BaseActivityComponent {
    void inject(BaseActivity activity);
}
