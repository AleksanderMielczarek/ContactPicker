package com.github.aleksandermielczarek.contactpickerexample.ui.main;

import android.databinding.ObservableField;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.github.aleksandermielczarek.contactpicker.ui.contacts.ContactsActivity_;

import org.parceler.Parcel;

import javax.inject.Inject;

/**
 * Created by Aleksander Mielczarek on 03.12.2016.
 */

public class MainViewModel {

    public static final int REQUEST_PICK_CONTACT = 1;

    public final ObservableField<String> name = new ObservableField<>();

    private final AppCompatActivity activity;

    @Inject
    public MainViewModel(AppCompatActivity activity) {
        this.activity = activity;
    }

    public void pickContact() {
        ContactsActivity_.intent(activity)
                .startForResult(REQUEST_PICK_CONTACT);
    }

    public void restoreState(@Nullable State state) {
        if (state != null) {
            name.set(state.getName());
        }
    }

    public State saveState() {
        return new State(name.get());
    }

    @Parcel
    public static final class State {

        String name;

        public State() {
        }

        public State(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

    }
}
