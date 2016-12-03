package com.github.aleksandermielczarek.contactpicker.domain;

import java.util.Arrays;

import javax.inject.Inject;

import rx.Observable;

/**
 * Created by Aleksander Mielczarek on 03.12.2016.
 */
public class ContactsServiceImpl implements ContactsService {

    @Inject
    public ContactsServiceImpl() {
    }

    @Override
    public Observable<Contact> loadContacts() {
        return Observable.fromCallable(() -> Arrays.asList(new Contact("Jan", "Kowalski"), new Contact("John", "Doe")))
                .flatMap(Observable::from);
    }
}
