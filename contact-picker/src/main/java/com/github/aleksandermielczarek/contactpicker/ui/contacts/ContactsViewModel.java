package com.github.aleksandermielczarek.contactpicker.ui.contacts;

import android.app.Activity;
import android.content.Intent;
import android.databinding.ObservableArrayList;
import android.databinding.ObservableInt;
import android.databinding.ObservableList;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;

import com.github.aleksandermielczarek.contactpicker.BR;
import com.github.aleksandermielczarek.contactpicker.R;
import com.github.aleksandermielczarek.contactpicker.domain.Contact;
import com.github.aleksandermielczarek.contactpicker.domain.ContactRepository;

import java.util.List;

import javax.inject.Inject;

import me.tatarka.bindingcollectionadapter.BaseItemViewSelector;
import me.tatarka.bindingcollectionadapter.ItemView;
import me.tatarka.bindingcollectionadapter.ItemViewSelector;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Aleksander Mielczarek on 03.12.2016.
 */

public class ContactsViewModel {

    public final ObservableList<ContactViewModel> contacts = new ObservableArrayList<>();
    public final ItemViewSelector<ContactViewModel> contactItemView = new BaseItemViewSelector<ContactViewModel>() {
        @Override
        public void select(ItemView itemView, int position, ContactViewModel item) {
            itemView.setBindingVariable(BR.viewModel);
            if (!item.contact.get().isPrimaryNumber()) {
                itemView.setLayoutRes(R.layout.item_contact_no_photo);
            } else if (TextUtils.isEmpty(item.contact.get().getPhoto())) {
                itemView.setLayoutRes(R.layout.item_contact_name_photo);
            } else {
                itemView.setLayoutRes(R.layout.item_contact_photo);
            }
        }
    };
    public final ObservableInt numberOfChosenContacts = new ObservableInt();

    private final ContactRepository contactRepository;
    private final ContactViewModelFactory contactViewModelFactory;
    private final AppCompatActivity activity;

    private ContactsViewModelListener viewModelListener;

    @Inject
    public ContactsViewModel(ContactRepository contactRepository, ContactViewModelFactory contactViewModelFactory, AppCompatActivity activity) {
        this.contactRepository = contactRepository;
        this.contactViewModelFactory = contactViewModelFactory;
        this.activity = activity;
    }

    public void askForContactsPermissions() {
        viewModelListener.askForContactsPermissions();
    }

    public void loadContacts() {
        contactRepository.findAll()
                .map(contact -> contactViewModelFactory.create(this, contact))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(contacts::add);
    }

    public void sendChosenContacts() {
        Observable.from(contacts)
                .filter(contactViewModel -> contactViewModel.chosen.get())
                .map(contactViewModel -> contactViewModel.contact.get())
                .toList()
                .subscribe(chosenContacts -> {
                    Intent intent = viewModelListener.prepareIntent(chosenContacts);
                    activity.setResult(Activity.RESULT_OK, intent);
                    activity.finish();
                });
    }

    public void chooseAllContacts() {
        Observable.from(contacts)
                .filter(contactViewModel -> !contactViewModel.chosen.get())
                .forEach(ContactViewModel::pickContact);
    }

    public void deselectAllContacts() {
        Observable.from(contacts).forEach(contactViewModel -> {
            contactViewModel.chosen.set(false);
            contactViewModel.selected.set(false);
        });
        numberOfChosenContacts.set(0);
    }

    public ContactsViewModelListener getViewModelListener() {
        return viewModelListener;
    }

    public void setViewModelListener(ContactsViewModelListener viewModelListener) {
        this.viewModelListener = viewModelListener;
    }

    public interface ContactsViewModelListener {

        void askForContactsPermissions();

        Intent prepareIntent(List<Contact> contacts);

        boolean multipleChoiceEnabled();

        void enableMultipleChoice();

        void disableMultipleChoice();
    }
}
