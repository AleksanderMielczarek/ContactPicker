package com.github.aleksandermielczarek.contactpicker.ui.contacts;

import android.app.Activity;
import android.content.Intent;
import android.databinding.ObservableInt;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.util.DiffUtil;
import android.text.TextUtils;
import android.util.Pair;

import com.github.aleksandermielczarek.contactpicker.BR;
import com.github.aleksandermielczarek.contactpicker.R;
import com.github.aleksandermielczarek.contactpicker.domain.Contact;
import com.github.aleksandermielczarek.contactpicker.domain.ContactRepository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import collections.DiffObservableList;
import me.tatarka.bindingcollectionadapter.OnItemBind;
import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by Aleksander Mielczarek on 03.12.2016.
 */

public class ContactsViewModel {

    public final DiffObservableList<ContactViewModel> contacts = new DiffObservableList<>(new DiffObservableList.Callback<ContactViewModel>() {
        @Override
        public boolean areItemsTheSame(ContactViewModel oldItem, ContactViewModel newItem) {
            return oldItem.equals(newItem);
        }

        @Override
        public boolean areContentsTheSame(ContactViewModel oldItem, ContactViewModel newItem) {
            return oldItem.equals(newItem);
        }
    });
    public final OnItemBind<ContactViewModel> contactItemView = (itemBinding, position, item) -> {
        itemBinding.variableId(BR.viewModel);
        if (!item.contact.get().isPrimaryNumber()) {
            itemBinding.layoutRes(R.layout.item_contact_no_photo);
        } else if (TextUtils.isEmpty(item.contact.get().getPhoto())) {
            itemBinding.layoutRes(R.layout.item_contact_name_photo);
        } else {
            itemBinding.layoutRes(R.layout.item_contact_photo);
        }
    };
    public final ObservableInt numberOfChosenContacts = new ObservableInt();

    private final List<ContactViewModel> allContacts = new ArrayList<>();
    private final ContactRepository contactRepository;
    private final AppCompatActivity activity;
    private final CompositeSubscription subscriptions = new CompositeSubscription();

    private Subscription searchSubscription;
    private ContactsViewModelListener viewModelListener;

    @Inject
    public ContactsViewModel(ContactRepository contactRepository, AppCompatActivity activity) {
        this.contactRepository = contactRepository;
        this.activity = activity;
    }

    public void askForContactsPermissions() {
        viewModelListener.askForContactsPermissions();
    }

    public void loadContacts() {
        subscriptions.add(contactRepository.findAll()
                .map(contact -> new ContactViewModel(this, contact))
                .toList()
                .doOnNext(allContacts::addAll)
                .map(contacts::calculateDiff)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(diffResult -> contacts.update(allContacts, diffResult)));
    }

    public void sendChosenContacts() {
        subscriptions.add(Observable.from(contacts)
                .filter(contactViewModel -> contactViewModel.selected.get())
                .map(contactViewModel -> contactViewModel.contact.get())
                .toList()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::sendContacts));
    }

    public void sendContacts(List<Contact> contacts) {
        Intent intent = viewModelListener.prepareIntent(contacts);
        activity.setResult(Activity.RESULT_OK, intent);
        activity.finish();
    }

    public void sendContact(Contact contact) {
        sendContacts(Collections.singletonList(contact));
    }

    public void chooseAllContacts() {
        subscriptions.add(Observable.from(contacts)
                .filter(contactViewModel -> !contactViewModel.selected.get())
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(ContactViewModel::pickContact));
    }

    public void deselectAllContacts() {
        subscriptions.add(Observable.from(contacts)
                .filter(contactViewModel -> contactViewModel.selected.get())
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(contactViewModel -> contactViewModel.selected.set(false),
                        throwable -> {
                            //do nothing
                        },
                        () -> numberOfChosenContacts.set(0)));
    }

    public void filterContacts(Observable<CharSequence> contactQuery) {
        searchSubscription = contactQuery
                .observeOn(Schedulers.newThread())
                .map(CharSequence::toString)
                .map(String::toLowerCase)
                .flatMap(query -> Observable.from(allContacts)
                        .filter(contact -> contact.contact.get().getName().toLowerCase().contains(query))
                        .toList())
                .map(newContacts -> {
                    DiffUtil.DiffResult diffResult = contacts.calculateDiff(newContacts);
                    return Pair.create(newContacts, diffResult);
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(listDiffResultPair -> contacts.update(listDiffResultPair.first, listDiffResultPair.second));
    }

    public void restoreContacts() {
        subscriptions.add(Observable.just(allContacts)
                .map(newContacts -> {
                    DiffUtil.DiffResult diffResult = contacts.calculateDiff(newContacts);
                    return Pair.create(newContacts, diffResult);
                })
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(listDiffResultPair -> contacts.update(listDiffResultPair.first, listDiffResultPair.second)));
    }

    public void enableSearchMode() {
        viewModelListener.enableSearchMode();
    }

    public void unubscribeSearch() {
        searchSubscription.unsubscribe();
    }

    public void unsubscribe() {
        subscriptions.clear();
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

        boolean multipleChoiceModeEnabled();

        void enableMultipleChoiceMode();

        void disableMultipleChoiceMode();

        boolean searchModeEnabled();

        void enableSearchMode();

        void disableSearchMode();
    }
}
