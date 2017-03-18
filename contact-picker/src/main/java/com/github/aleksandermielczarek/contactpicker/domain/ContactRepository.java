package com.github.aleksandermielczarek.contactpicker.domain;

import io.reactivex.Observable;

/**
 * Created by Aleksander Mielczarek on 03.12.2016.
 */

public interface ContactRepository {

    Observable<Contact> findAll();

}
