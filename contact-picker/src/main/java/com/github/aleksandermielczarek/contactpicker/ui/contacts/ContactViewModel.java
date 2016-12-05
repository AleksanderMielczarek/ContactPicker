package com.github.aleksandermielczarek.contactpicker.ui.contacts;

import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;

import com.github.aleksandermielczarek.contactpicker.domain.Contact;
import com.google.auto.factory.AutoFactory;

/**
 * Created by Aleksander Mielczarek on 03.12.2016.
 */
@AutoFactory
public class ContactViewModel {

    public final ObservableField<Contact> contact = new ObservableField<>();
    public final ObservableBoolean chosen = new ObservableBoolean(false);
    public final ObservableBoolean selected = new ObservableBoolean(false);

    private final ContactsViewModel contactsViewModel;

    public ContactViewModel(ContactsViewModel contactsViewModel, Contact contact) {
        this.contactsViewModel = contactsViewModel;
        this.contact.set(contact);
    }

    public void pickContact() {
        if (contactsViewModel.getViewModelListener().multipleChoiceEnabled()) {
            selected.set(!selected.get());
            if (chosen.get()) {
                contactsViewModel.numberOfChosenContacts.set(contactsViewModel.numberOfChosenContacts.get() - 1);
                if (contactsViewModel.numberOfChosenContacts.get() == 0) {
                    contactsViewModel.getViewModelListener().disableMultipleChoice();
                }
            } else {
                contactsViewModel.numberOfChosenContacts.set(contactsViewModel.numberOfChosenContacts.get() + 1);
            }
            chosen.set(!chosen.get());
        } else {
            chosen.set(true);
            contactsViewModel.sendChosenContacts();
        }
    }

    public boolean pickMultipleContacts() {
        if (!contactsViewModel.getViewModelListener().multipleChoiceEnabled()) {
            chosen.set(true);
            selected.set(true);
            contactsViewModel.numberOfChosenContacts.set(contactsViewModel.numberOfChosenContacts.get() + 1);
            contactsViewModel.getViewModelListener().enableMultipleChoice();
            return true;
        }
        return false;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ContactViewModel that = (ContactViewModel) o;

        return contact.get().equals(that.contact.get());
    }

    @Override
    public int hashCode() {
        return contact.get().hashCode();
    }
}
