package com.github.aleksandermielczarek.contactpicker.ui.contacts;

import android.Manifest;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.github.aleksandermielczarek.contactpicker.R;
import com.github.aleksandermielczarek.contactpicker.component.DaggerActivityComponent;
import com.github.aleksandermielczarek.contactpicker.databinding.ActivityContactsBinding;
import com.github.aleksandermielczarek.contactpicker.domain.Contact;
import com.github.aleksandermielczarek.contactpicker.module.ActivityModule;
import com.github.aleksandermielczarek.permissionsdialogs.PermissionsDialogs;

import org.androidannotations.annotations.EActivity;

import javax.inject.Inject;

import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.OnNeverAskAgain;
import permissions.dispatcher.OnShowRationale;
import permissions.dispatcher.PermissionRequest;
import permissions.dispatcher.RuntimePermissions;

/**
 * Created by Aleksander Mielczarek on 03.12.2016.
 */
@EActivity
@RuntimePermissions
public class ContactsActivity extends AppCompatActivity implements ContactsViewModel.ContactsViewModelListener {

    public static final String EXTRA_CONTACT_NAME = "extraContactName";

    @Inject
    protected ContactsViewModel contactsViewModel;

    private ActivityContactsBinding binding;

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
    protected void onStart() {
        super.onStart();
        contactsViewModel.askForContactsPermissions();
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
    public void contactPicked(Contact contact) {
        Intent intent = new Intent();
        intent.putExtra(EXTRA_CONTACT_NAME, contact.getName());
        setResult(RESULT_OK, intent);
        finish();
    }
}
