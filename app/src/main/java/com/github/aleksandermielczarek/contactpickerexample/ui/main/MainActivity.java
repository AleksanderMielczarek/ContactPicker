package com.github.aleksandermielczarek.contactpickerexample.ui.main;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.github.aleksandermielczarek.contactpicker.domain.Contact;
import com.github.aleksandermielczarek.contactpicker.ui.contacts.ContactsActivity;
import com.github.aleksandermielczarek.contactpickerexample.R;
import com.github.aleksandermielczarek.contactpickerexample.component.AppComponent;
import com.github.aleksandermielczarek.contactpickerexample.databinding.ActivityMainBinding;
import com.github.aleksandermielczarek.napkin.Napkin;
import com.github.aleksandermielczarek.napkin.module.NapkinActivityModule;

import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.InstanceState;
import org.androidannotations.annotations.OnActivityResult;
import org.parceler.Parcels;

import java.util.ArrayList;

import javax.inject.Inject;

/**
 * Created by Aleksander Mielczarek on 03.12.2016.
 */
@EActivity
public class MainActivity extends AppCompatActivity {

    @InstanceState
    protected MainViewModel.State state;

    @Inject
    protected MainViewModel mainViewModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Napkin.<AppComponent>provideAppComponent(this)
                .with(new NapkinActivityModule(this))
                .inject(this);
        mainViewModel.restoreState(state);
        ActivityMainBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        binding.setViewModel(mainViewModel);
        setSupportActionBar(binding.toolbar);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        state = mainViewModel.saveState();
    }

    @OnActivityResult(MainViewModel.REQUEST_PICK_CONTACT)
    protected void pickContact(int result, @OnActivityResult.Extra(ContactsActivity.EXTRA_CONTACTS) ArrayList<Parcelable> contacts) {
        if (result == RESULT_OK) {
            Parcelable parcelable = contacts.get(0);
            Contact contact = Parcels.unwrap(parcelable);
            mainViewModel.name.set(contact.getName());
        }
    }
}
