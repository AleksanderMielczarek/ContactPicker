package com.github.aleksandermielczarek.contactpicker.module;

import com.github.aleksandermielczarek.contactpicker.domain.ContactsService;
import com.github.aleksandermielczarek.contactpicker.domain.ContactsServiceImpl;

import dagger.Binds;
import dagger.Module;

/**
 * Created by Aleksander Mielczarek on 03.12.2016.
 */
@Module
public abstract class DomainModule {

    @Binds
    abstract ContactsService bindContactService(ContactsServiceImpl contactsService);
}
