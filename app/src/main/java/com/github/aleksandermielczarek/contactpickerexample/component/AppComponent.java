package com.github.aleksandermielczarek.contactpickerexample.component;

import com.github.aleksandermielczarek.napkin.module.NapkinActivityModule;
import com.github.aleksandermielczarek.napkin.module.NapkinAppModule;
import com.github.aleksandermielczarek.napkin.scope.AppScope;

import dagger.Component;

/**
 * Created by Aleksander Mielczarek on 03.12.2016.
 */
@AppScope
@Component(modules = NapkinAppModule.class)
public interface AppComponent {

    ActivityComponent with(NapkinActivityModule napkinActivityModule);
}
