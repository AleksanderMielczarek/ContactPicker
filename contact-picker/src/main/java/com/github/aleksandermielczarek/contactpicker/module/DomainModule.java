package com.github.aleksandermielczarek.contactpicker.module;

import com.github.aleksandermielczarek.contactpicker.domain.ContactRepository;
import com.github.aleksandermielczarek.contactpicker.domain.ContactRepositoryImpl;

import dagger.Binds;
import dagger.Module;

/**
 * Created by Aleksander Mielczarek on 03.12.2016.
 */
@Module
public abstract class DomainModule {

    @Binds
    abstract ContactRepository bindContactRepository(ContactRepositoryImpl contactRepository);
}
