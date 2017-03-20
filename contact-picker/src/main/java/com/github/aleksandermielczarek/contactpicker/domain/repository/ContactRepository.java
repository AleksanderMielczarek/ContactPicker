package com.github.aleksandermielczarek.contactpicker.domain.repository;

import com.github.aleksandermielczarek.contactpicker.domain.data.Contact;

import java.util.List;

import io.reactivex.Single;

/**
 * Created by Aleksander Mielczarek on 03.12.2016.
 */

public interface ContactRepository {

    Single<List<Contact>> findAll();

}
