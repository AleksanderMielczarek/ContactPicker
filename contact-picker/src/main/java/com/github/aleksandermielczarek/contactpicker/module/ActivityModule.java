package com.github.aleksandermielczarek.contactpicker.module;

import android.content.ContentResolver;
import android.support.v7.app.AppCompatActivity;

import com.github.aleksandermielczarek.napkin.module.NapkinActivityModule;
import com.github.aleksandermielczarek.napkin.scope.ActivityScope;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Aleksander Mielczarek on 03.12.2016.
 */
@Module
@ActivityScope
public class ActivityModule extends NapkinActivityModule {

    public ActivityModule(AppCompatActivity activity) {
        super(activity);
    }

    @Provides
    ContentResolver provideContentResolver() {
        return activity.getContentResolver();
    }
}
