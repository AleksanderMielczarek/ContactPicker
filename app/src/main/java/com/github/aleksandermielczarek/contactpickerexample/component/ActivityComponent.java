package com.github.aleksandermielczarek.contactpickerexample.component;

import com.github.aleksandermielczarek.contactpickerexample.ui.main.MainActivity;
import com.github.aleksandermielczarek.napkin.module.NapkinActivityModule;
import com.github.aleksandermielczarek.napkin.scope.ActivityScope;

import dagger.Subcomponent;

/**
 * Created by Aleksander Mielczarek on 03.12.2016.
 */
@ActivityScope
@Subcomponent(modules = NapkinActivityModule.class)
public interface ActivityComponent {

    void inject(MainActivity mainActivity);
}
