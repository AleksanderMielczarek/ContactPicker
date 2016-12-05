package com.github.aleksandermielczarek.contactpicker.ui.contacts;

import android.app.Activity;
import android.content.Intent;
import android.databinding.ObservableArrayList;
import android.databinding.ObservableInt;
import android.databinding.ObservableList;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.util.DiffUtil;
import android.support.v7.util.ListUpdateCallback;
import android.text.TextUtils;
import android.util.Log;

import com.github.aleksandermielczarek.contactpicker.BR;
import com.github.aleksandermielczarek.contactpicker.R;
import com.github.aleksandermielczarek.contactpicker.domain.Contact;
import com.github.aleksandermielczarek.contactpicker.domain.ContactRepository;

import java.text.Collator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ListIterator;

import javax.inject.Inject;

import me.tatarka.bindingcollectionadapter.BaseItemViewSelector;
import me.tatarka.bindingcollectionadapter.ItemView;
import me.tatarka.bindingcollectionadapter.ItemViewSelector;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

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

    private final List<Contact> allContacts = new ArrayList<>();
    private final ContactRepository contactRepository;
    private final ContactViewModelFactory contactViewModelFactory;
    private final AppCompatActivity activity;
    private final CompositeSubscription subscriptions = new CompositeSubscription();

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
        subscriptions.add(contactRepository.findAll()
                .doOnNext(allContacts::add)
                .map(contact -> contactViewModelFactory.create(this, contact))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(contacts::add));
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

    public void filterContacts(Observable<CharSequence> contactQuery) {
        subscriptions.add(contactQuery
                .observeOn(Schedulers.newThread())
                .map(CharSequence::toString)
                .map(String::toLowerCase)
                .flatMap(query -> Observable.from(allContacts)
                        .filter(contact -> contact.getName().toLowerCase().contains(query))
                        .map(contact -> contactViewModelFactory.create(this, contact))
                        .toList())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(newContacts -> {
                    if (newContacts.isEmpty()) {
                        contacts.clear();
                        return;
                    }
                    ListIterator<ContactViewModel> contactViewModelListIterator = contacts.listIterator();
                    while (contactViewModelListIterator.hasNext()) {
                        ContactViewModel contactViewModel = contactViewModelListIterator.next();
                        if (newContacts.contains(contactViewModel)) {
                            int indexToRemove = newContacts.indexOf(contactViewModel);
                            newContacts.remove(indexToRemove);
                        } else {
                            contactViewModelListIterator.remove();
                        }
                    }
                    if (!newContacts.isEmpty()) {
                        contacts.addAll(newContacts);
                    }
                    Collections.sort(contacts, (contactViewModel1, contactViewModel2) -> {
                        Collator collator = Collator.getInstance();
                        collator.setStrength(Collator.PRIMARY);
                        return collator.compare(contactViewModel1.contact.get().getName(), contactViewModel2.contact.get().getName());
                    });
                }));
                /*.observeOn(AndroidSchedulers.mainThread())
                .subscribe(newContacts -> {
                    DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(new DiffUtil.Callback() {
                        @Override
                        public int getOldListSize() {
                            return contacts.size();
                        }

                        @Override
                        public int getNewListSize() {
                            return newContacts.size();
                        }

                        @Override
                        public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
                            return contacts.get(oldItemPosition).contact.get().equals(newContacts.get(newItemPosition).contact.get());
                        }

                        @Override
                        public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
                            return contacts.get(oldItemPosition).contact.get().equals(newContacts.get(newItemPosition).contact.get());
                        }
                    });
                    diffResult.dispatchUpdatesTo(new ListUpdateCallback() {
                        @Override
                        public void onInserted(int position, int count) {
                            for (int i = 0; i < count; i++) {
                                int positionOld = position + i;
                                int newSize = contacts.size();
                                int oldSize = newContacts.size();
                                ContactViewModel newModel = newContacts.get(positionOld);
                                String name = newModel.contact.get().getName();
                                Log.e("DIFF", "positionNew: " + position + ", positionOld: " + positionOld + ", sizeNew: " + newSize + ", sizeOld: " + oldSize + ", name: " + name);
                                contacts.set(position-1, newModel);
                            }
                        }

                        @Override
                        public void onRemoved(int position, int count) {
                            for (int i = 1; i <= count; i++) {
                                contacts.remove(position);
                            }
                        }

                        @Override
                        public void onMoved(int fromPosition, int toPosition) {
                            ContactViewModel moved = contacts.remove(fromPosition);
                            contacts.add(toPosition, moved);
                        }

                        @Override
                        public void onChanged(int position, int count, Object payload) {
                            for (int i = 0; i < count; i++) {
                                contacts.set(position + i, (ContactViewModel) payload);
                            }
                        }
                    });
                }));*/
                /*.subscribe(diffResultPair -> {
                    listDiffResultPair.second.dispatchUpdatesTo(new ListUpdateCallback() {
                        @Override
                        public void onInserted(int position, int count) {
                            for (int i = 0; i < count; i++) {
                                contacts.add(position + i, listDiffResultPair.first.get(position + i));
                            }
                        }

                        @Override
                        public void onRemoved(int position, int count) {
                            for (int i = 1; i <= count; i++) {
                                contacts.remove(position);
                            }
                        }

                        @Override
                        public void onMoved(int fromPosition, int toPosition) {
                            ContactViewModel moved = contacts.remove(fromPosition);
                            contacts.add(toPosition, moved);
                        }

                        @Override
                        public void onChanged(int position, int count, Object payload) {
                            for (int i = 0; i < count; i++) {
                                contacts.set(position + i, (ContactViewModel) payload);
                            }
                        }
                    });
                }));*/
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

        boolean multipleChoiceEnabled();

        void enableMultipleChoice();

        void disableMultipleChoice();

        void dispatchUpdates(DiffUtil.DiffResult diffResult);
    }
}
