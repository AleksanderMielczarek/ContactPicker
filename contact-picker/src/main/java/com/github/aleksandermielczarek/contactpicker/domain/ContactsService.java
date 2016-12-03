package com.github.aleksandermielczarek.contactpicker.domain;

import rx.Observable;

/**
 * Created by Aleksander Mielczarek on 03.12.2016.
 */

public interface ContactsService {

    Observable<Contact> loadContacts();
}
