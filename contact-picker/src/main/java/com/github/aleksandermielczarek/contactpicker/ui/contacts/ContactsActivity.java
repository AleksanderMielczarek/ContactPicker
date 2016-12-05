package com.github.aleksandermielczarek.contactpicker.ui.contacts;

import android.Manifest;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.SearchView;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.github.aleksandermielczarek.contactpicker.R;
import com.github.aleksandermielczarek.contactpicker.component.DaggerActivityComponent;
import com.github.aleksandermielczarek.contactpicker.databinding.ActivityContactsBinding;
import com.github.aleksandermielczarek.contactpicker.databinding.MenuChosenCounterBinding;
import com.github.aleksandermielczarek.contactpicker.domain.Contact;
import com.github.aleksandermielczarek.contactpicker.module.ActivityModule;
import com.github.aleksandermielczarek.permissionsdialogs.PermissionsDialogs;
import com.jakewharton.rxbinding.support.v7.widget.RxSearchView;

import org.androidannotations.annotations.EActivity;
import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.OnNeverAskAgain;
import permissions.dispatcher.OnShowRationale;
import permissions.dispatcher.PermissionRequest;
import permissions.dispatcher.RuntimePermissions;
import rx.Observable;

/**
 * Created by Aleksander Mielczarek on 03.12.2016.
 */
@EActivity
@RuntimePermissions
public class ContactsActivity extends AppCompatActivity implements ContactsViewModel.ContactsViewModelListener {

    public static final String EXTRA_CONTACTS = "extraContacts";

    @Inject
    protected ContactsViewModel contactsViewModel;

    private ActivityContactsBinding binding;
    private boolean multipleChoiceEnabled = false;
    private ActionMode multipleChoiceActionMode;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DaggerActivityComponent.builder()
                .activityModule(new ActivityModule(this))
                .build()
                .inject(this);
        contactsViewModel.setViewModelListener(this);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_contacts);
        binding.setViewModel(contactsViewModel);
        setSupportActionBar(binding.toolbar);
        binding.toolbar.setTitle(getString(R.string.title));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_search, menu);

        MenuItem searchItem = menu.findItem(R.id.menu_action_search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        contactsViewModel.filterContacts(RxSearchView.queryTextChanges(searchView));
        return true;
    }

    @Override
    protected void onStart() {
        super.onStart();
        contactsViewModel.askForContactsPermissions();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        contactsViewModel.unsubscribe();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        ContactsActivityPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
    }

    @NeedsPermission(Manifest.permission.READ_CONTACTS)
    protected void loadContacts() {
        contactsViewModel.loadContacts();
    }

    @OnShowRationale(Manifest.permission.READ_CONTACTS)
    protected void showRationaleForReadContacts(PermissionRequest request) {
        PermissionsDialogs.showRationaleDialog(this, request, R.string.permission_read_contacts_rationale, R.string.dialog_ok, R.string.dialog_cancel);
    }

    @OnNeverAskAgain(Manifest.permission.READ_CONTACTS)
    protected void showNeverAskForReadContacts() {
        PermissionsDialogs.showNeverAskAgainDialog(this, R.string.permission_read_contacts_do_not_ask_again, R.string.dialog_ok, R.string.dialog_cancel);
    }

    @Override
    public void askForContactsPermissions() {
        ContactsActivityPermissionsDispatcher.loadContactsWithCheck(this);
    }

    @Override
    public Intent prepareIntent(List<Contact> contacts) {
        Intent intent = new Intent();
        ArrayList<Parcelable> parcelableContacts = new ArrayList<>(contacts.size());
        Observable.from(contacts)
                .map(Parcels::wrap)
                .forEach(parcelableContacts::add);
        intent.putParcelableArrayListExtra(EXTRA_CONTACTS, parcelableContacts);
        return intent;
    }

    @Override
    public boolean multipleChoiceEnabled() {
        return multipleChoiceEnabled;
    }

    @Override
    public void enableMultipleChoice() {
        startActionMode(new ActionMode.Callback() {
            @Override
            public boolean onCreateActionMode(ActionMode actionMode, Menu menu) {
                MenuChosenCounterBinding counterBinding = MenuChosenCounterBinding.inflate(getLayoutInflater());
                counterBinding.setViewModel(contactsViewModel);
                actionMode.setCustomView(counterBinding.getRoot());
                MenuInflater menuInflater = getMenuInflater();
                menuInflater.inflate(R.menu.menu_select_multiple, menu);
                multipleChoiceEnabled = true;
                multipleChoiceActionMode = actionMode;
                return true;
            }

            @Override
            public boolean onPrepareActionMode(ActionMode actionMode, Menu menu) {
                return false;
            }

            @Override
            public boolean onActionItemClicked(ActionMode actionMode, MenuItem menuItem) {
                if (menuItem.getItemId() == R.id.menu_action_choose) {
                    contactsViewModel.sendChosenContacts();
                    actionMode.finish();
                    return true;
                } else if (menuItem.getItemId() == R.id.menu_action_select_all) {
                    contactsViewModel.chooseAllContacts();
                    return true;
                }
                return false;
            }

            @Override
            public void onDestroyActionMode(ActionMode actionMode) {
                contactsViewModel.deselectAllContacts();
                multipleChoiceEnabled = false;
                multipleChoiceActionMode = null;
            }
        });
    }

    @Override
    public void disableMultipleChoice() {
        if (multipleChoiceActionMode != null) {
            multipleChoiceActionMode.finish();
        }
    }

    @Override
    public void dispatchUpdates(DiffUtil.DiffResult diffResult) {
        diffResult.dispatchUpdatesTo(binding.contactsRecycler.getAdapter());
    }

}
