package com.github.aleksandermielczarek.contactpicker.ui.contacts;

import android.databinding.ObservableArrayList;
import android.databinding.ObservableList;

import com.github.aleksandermielczarek.contactpicker.BR;
import com.github.aleksandermielczarek.contactpicker.R;
import com.github.aleksandermielczarek.contactpicker.domain.Contact;
import com.github.aleksandermielczarek.contactpicker.domain.ContactRepository;

import javax.inject.Inject;

import me.tatarka.bindingcollectionadapter.ItemView;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Aleksander Mielczarek on 03.12.2016.
 */

public class ContactsViewModel {

    public final ObservableList<ContactViewModel> contacts = new ObservableArrayList<>();
    public final ItemView contactItemView = ItemView.of(BR.viewModel, R.layout.item_contact);

    private final ContactRepository contactRepository;
    private final ContactViewModelFactory contactViewModelFactory;

    private ContactsViewModelListener viewModelListener;

    @Inject
    public ContactsViewModel(ContactRepository contactRepository, ContactViewModelFactory contactViewModelFactory) {
        this.contactRepository = contactRepository;
        this.contactViewModelFactory = contactViewModelFactory;
    }

    public void setViewModelListener(ContactsViewModelListener viewModelListener) {
        this.viewModelListener = viewModelListener;
    }

    public void askForContactsPermissions() {
        viewModelListener.askForContactsPermissions();
    }

    public void loadContacts() {
        contactRepository.findAll()
                .map(contact -> contactViewModelFactory.create(contact, viewModelListener))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(contacts::add);
    }

    public interface ContactsViewModelListener {

        void askForContactsPermissions();

        void contactPicked(Contact contact);
    }
}
