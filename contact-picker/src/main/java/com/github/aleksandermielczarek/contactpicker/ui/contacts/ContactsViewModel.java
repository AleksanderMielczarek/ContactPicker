package com.github.aleksandermielczarek.contactpicker.ui.contacts;

import android.databinding.ObservableInt;
import android.support.annotation.Nullable;
import android.support.v7.util.DiffUtil;
import android.util.Pair;

import com.github.aleksandermielczarek.contactpicker.BR;
import com.github.aleksandermielczarek.contactpicker.R;
import com.github.aleksandermielczarek.contactpicker.domain.data.Contact;
import com.github.aleksandermielczarek.contactpicker.domain.repository.ContactRepository;

import org.parceler.Parcel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Predicate;
import io.reactivex.schedulers.Schedulers;
import me.tatarka.bindingcollectionadapter2.ItemBinding;
import me.tatarka.bindingcollectionadapter2.collections.DiffObservableList;

/**
 * Created by Aleksander Mielczarek on 03.12.2016.
 */

public final class ContactsViewModel {

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
    public final ItemBinding<ContactViewModel> contactItemView = ItemBinding.of(BR.viewModel, R.layout.item_contact);
    public final ObservableInt numberOfChosenContacts = new ObservableInt();

    private final List<ContactViewModel> allContacts = new ArrayList<>();
    private final ContactRepository contactRepository;
    private final CompositeDisposable disposables = new CompositeDisposable();

    private Disposable searchDisposable;
    private ContactsViewModelListener viewModelListener;
    private String query = "";
    private List<Long> selectedContacts;
    private boolean multipleChoiceEnabled = false;
    private boolean searchModeEnabled = false;

    @Inject
    public ContactsViewModel(ContactRepository contactRepository) {
        this.contactRepository = contactRepository;
    }

    public void loadContacts(List<Long> selectedContacts, @Nullable State state) {
        restore(selectedContacts, state);
        disposables.add(contactRepository.findAll()
                .flatMap(contacts -> Observable.fromIterable(contacts)
                        .map(contact -> new ContactViewModel(this, contact, this.selectedContacts.contains(contact.getId())))
                        .toList())
                .doOnSuccess(allContacts::addAll)
                .flatMapObservable(Observable::fromIterable)
                .filter(filterContactsByQuery(query))
                .toList()
                .map(newContacts -> {
                    DiffUtil.DiffResult diffResult = contacts.calculateDiff(newContacts);
                    return Pair.create(newContacts, diffResult);
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(listDiffResultPair -> {
                    contacts.update(listDiffResultPair.first, listDiffResultPair.second);
                    viewModelListener.restoreScrollPosition();
                    if (searchModeEnabled) {
                        viewModelListener.enableSearchMode(query);
                    }
                    if (multipleChoiceEnabled) {
                        enableMultipleChoiceMode();
                    }
                }, viewModelListener::showError));
    }

    private void restore(List<Long> selectedContacts, @Nullable State state) {
        if (state == null) {
            this.selectedContacts = selectedContacts;
        } else {
            this.selectedContacts = state.getSelectedContacts();
            query = state.getQuery();
            multipleChoiceEnabled = state.isMultipleChoiceEnabled();
            searchModeEnabled = state.isSearchModeEnabled();
        }
        numberOfChosenContacts.set(this.selectedContacts.size());
    }

    public void sendChosenContacts() {
        disposables.add(Observable.fromIterable(contacts)
                .filter(contactViewModel -> contactViewModel.selected.get())
                .map(contactViewModel -> contactViewModel.contact.get())
                .toList()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::sendContacts, viewModelListener::showError));
    }

    public void sendContact(Contact contact) {
        sendContacts(Collections.singletonList(contact));
    }

    private void sendContacts(List<Contact> contacts) {
        viewModelListener.selectContacts(contacts);
    }

    public void chooseAllContacts() {
        disposables.add(Observable.fromIterable(contacts)
                .filter(contactViewModel -> !contactViewModel.selected.get())
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(ContactViewModel::pickContact));
    }

    public void deselectAllContacts() {
        disposables.add(Observable.fromIterable(contacts)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(contactViewModel -> {
                            contactViewModel.selected.set(false);
                            contactViewModel.multipleChoiceMode.set(false);
                        },
                        viewModelListener::showError,
                        () -> numberOfChosenContacts.set(0)));
    }

    public void filterContacts(Observable<CharSequence> contactQuery) {
        searchDisposable = contactQuery
                .observeOn(Schedulers.newThread())
                .map(CharSequence::toString)
                .doOnNext(this::setQuery)
                .flatMapSingle(query -> Observable.fromIterable(allContacts)
                        .filter(filterContactsByQuery(query))
                        .toList())
                .map(newContacts -> {
                    DiffUtil.DiffResult diffResult = contacts.calculateDiff(newContacts);
                    return Pair.create(newContacts, diffResult);
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(listDiffResultPair -> contacts.update(listDiffResultPair.first, listDiffResultPair.second),
                        viewModelListener::showError);
    }

    private Predicate<ContactViewModel> filterContactsByQuery(String query) {
        return contact -> contact.contact.get().getName().toLowerCase().contains(query.toLowerCase());
    }

    public void restoreContacts() {
        disposables.add(Observable.just(allContacts)
                .map(newContacts -> {
                    DiffUtil.DiffResult diffResult = contacts.calculateDiff(newContacts);
                    return Pair.create(newContacts, diffResult);
                })
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(listDiffResultPair -> contacts.update(listDiffResultPair.first, listDiffResultPair.second),
                        viewModelListener::showError));
    }

    public void enableMultipleChoiceMode() {
        viewModelListener.enableMultipleChoiceMode();
        disposables.add(Observable.fromIterable(contacts)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(contactViewModel -> contactViewModel.multipleChoiceMode.set(true),
                        viewModelListener::showError));
    }

    public void disposeSearch() {
        searchDisposable.dispose();
    }

    public void dispose() {
        disposables.clear();
    }

    public State saveState() {
        ArrayList<Long> selectedContacts = Observable.fromIterable(contacts)
                .filter(contactViewModel -> contactViewModel.selected.get())
                .map(contactViewModel -> contactViewModel.contact.get())
                .map(Contact::getId)
                .toList(ArrayList::new)
                .blockingGet();
        return new State(query, selectedContacts, viewModelListener.multipleChoiceModeEnabled(), viewModelListener.searchModeEnabled());
    }

    public ContactsViewModelListener getViewModelListener() {
        return viewModelListener;
    }

    public void setViewModelListener(ContactsViewModelListener viewModelListener) {
        this.viewModelListener = viewModelListener;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public String getQuery() {
        return query;
    }

    @Parcel(Parcel.Serialization.BEAN)
    public static class State {

        private String query;
        private ArrayList<Long> selectedContacts;
        private boolean multipleChoiceEnabled;
        private boolean searchModeEnabled;

        public State() {

        }

        public State(String query, ArrayList<Long> selectedContacts, boolean multipleChoiceEnabled, boolean searchModeEnabled) {
            this.query = query;
            this.selectedContacts = selectedContacts;
            this.multipleChoiceEnabled = multipleChoiceEnabled;
            this.searchModeEnabled = searchModeEnabled;
        }

        public String getQuery() {
            return query;
        }

        public void setQuery(String query) {
            this.query = query;
        }

        public ArrayList<Long> getSelectedContacts() {
            return selectedContacts;
        }

        public void setSelectedContacts(ArrayList<Long> selectedContacts) {
            this.selectedContacts = selectedContacts;
        }

        public boolean isMultipleChoiceEnabled() {
            return multipleChoiceEnabled;
        }

        public void setMultipleChoiceEnabled(boolean multipleChoiceEnabled) {
            this.multipleChoiceEnabled = multipleChoiceEnabled;
        }

        public boolean isSearchModeEnabled() {
            return searchModeEnabled;
        }

        public void setSearchModeEnabled(boolean searchModeEnabled) {
            this.searchModeEnabled = searchModeEnabled;
        }
    }

    public interface ContactsViewModelListener {

        void selectContacts(List<Contact> contacts);

        boolean multipleChoiceModeEnabled();

        void enableMultipleChoiceMode();

        void disableMultipleChoiceMode();

        boolean searchModeEnabled();

        void enableSearchMode(String query);

        void showError(Throwable throwable);

        void restoreScrollPosition();
    }
}
