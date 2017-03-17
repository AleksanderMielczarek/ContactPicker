package com.github.aleksandermielczarek.contactpicker.ui.contacts;

import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;

import com.github.aleksandermielczarek.contactpicker.domain.Contact;

/**
 * Created by Aleksander Mielczarek on 03.12.2016.
 */
public class ContactViewModel {

    public final ObservableField<Contact> contact = new ObservableField<>();
    public final ObservableBoolean selected = new ObservableBoolean(false);

    private final ContactsViewModel contactsViewModel;
    private final ContactsViewModel.ContactsViewModelListener contactsViewModelListener;

    public ContactViewModel(ContactsViewModel contactsViewModel, Contact contact) {
        this.contactsViewModel = contactsViewModel;
        this.contact.set(contact);
        contactsViewModelListener = contactsViewModel.getViewModelListener();
    }

    public void pickContact() {
        if (contactsViewModelListener.multipleChoiceModeEnabled()) {
            if (selected.get()) {
                contactsViewModel.numberOfChosenContacts.set(contactsViewModel.numberOfChosenContacts.get() - 1);
                if (contactsViewModel.numberOfChosenContacts.get() == 0) {
                    contactsViewModel.getViewModelListener().disableMultipleChoiceMode();
                }
            } else {
                contactsViewModel.numberOfChosenContacts.set(contactsViewModel.numberOfChosenContacts.get() + 1);
            }
            selected.set(!selected.get());
        } else {
            contactsViewModel.sendContact(contact.get());
        }
    }

    public boolean pickMultipleContacts() {
        if (!contactsViewModelListener.multipleChoiceModeEnabled()) {
            selected.set(true);
            contactsViewModel.numberOfChosenContacts.set(contactsViewModel.numberOfChosenContacts.get() + 1);
           contactsViewModelListener.enableMultipleChoiceMode();
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
