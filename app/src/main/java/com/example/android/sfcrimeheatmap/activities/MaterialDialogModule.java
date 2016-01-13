package com.example.android.sfcrimeheatmap.activities;

import com.afollestad.materialdialogs.MaterialDialog;
import com.example.android.sfcrimeheatmap.R;
import com.example.android.sfcrimeheatmap.di.annotations.ActivityScope;

import dagger.Module;
import dagger.Provides;

@Module
public class MaterialDialogModule {

    private BaseActivityCallbacks callbacks;

    public MaterialDialogModule(BaseActivityCallbacks callbacks) {
        this.callbacks = callbacks;
    }

    @Provides
    @ActivityScope
    public MaterialDialog provideDialog() {
        return new MaterialDialog.Builder(callbacks.getBaseActivityContext())
                .cancelable(false)
                .title(callbacks.getBaseActivityContext().getString(R.string.loading))
                .build();
    }
}
