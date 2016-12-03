package com.github.aleksandermielczarek.contactpicker.ui.contacts;

import android.databinding.ObservableField;

import com.github.aleksandermielczarek.contactpicker.domain.Contact;
import com.google.auto.factory.AutoFactory;

/**
 * Created by Aleksander Mielczarek on 03.12.2016.
 */
@AutoFactory
public class ContactViewModel {

    public final ObservableField<Contact> contact = new ObservableField<>();

    private final ContactsViewModel.ContactsViewModelListener viewModelListener;

    public ContactViewModel(Contact contact, ContactsViewModel.ContactsViewModelListener viewModelListener) {
        this.viewModelListener = viewModelListener;
        this.contact.set(contact);
    }

    public void pickContact() {
        viewModelListener.contactPicked(contact.get());
    }
}
