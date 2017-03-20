package com.github.aleksandermielczarek.contactpicker.component;

import com.github.aleksandermielczarek.contactpicker.module.ActivityModule;
import com.github.aleksandermielczarek.contactpicker.ui.contacts.ContactsActivity;
import com.github.aleksandermielczarek.napkin.scope.ActivityScope;

import dagger.Component;

/**
 * Created by Aleksander Mielczarek on 03.12.2016.
 */
@Component(modules = ActivityModule.class)
@ActivityScope
public interface ActivityComponent {

    void inject(ContactsActivity contactsActivity);
}
