package com.github.aleksandermielczarek.contactpickerexample;

import android.app.Application;

import com.github.aleksandermielczarek.contactpickerexample.component.AppComponent;
import com.github.aleksandermielczarek.contactpickerexample.component.DaggerAppComponent;
import com.github.aleksandermielczarek.napkin.ComponentProvider;
import com.github.aleksandermielczarek.napkin.module.NapkinAppModule;

/**
 * Created by Aleksander Mielczarek on 03.12.2016.
 */

public class ContactPickerApplication extends Application implements ComponentProvider<AppComponent> {

    private AppComponent appComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        appComponent = DaggerAppComponent.builder()
                .napkinAppModule(new NapkinAppModule(this))
                .build();
    }

    @Override
    public AppComponent provideComponent() {
        return appComponent;
    }
}
