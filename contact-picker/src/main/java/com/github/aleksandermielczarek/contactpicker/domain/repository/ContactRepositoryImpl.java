package com.github.aleksandermielczarek.contactpicker.domain.repository;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.provider.ContactsContract;

import com.github.aleksandermielczarek.contactpicker.domain.data.Contact;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;

import javax.inject.Inject;

import io.reactivex.Single;

/**
 * Created by Aleksander Mielczarek on 03.12.2016.
 */
public final class ContactRepositoryImpl implements ContactRepository {

    private final ContentResolver contentResolver;

    @Inject
    public ContactRepositoryImpl(Context context) {
        contentResolver = context.getContentResolver();

    }

    private Contact mapRowToContact(boolean primaryNumber, Cursor contactCursor, Cursor phoneCursor) {
        Contact contact = new Contact();
        contact.setId(phoneCursor.getLong(phoneCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone._ID)));
        contact.setName(contactCursor.getString(contactCursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME_PRIMARY)));
        contact.setPhoto(contactCursor.getString(contactCursor.getColumnIndex(ContactsContract.Contacts.PHOTO_URI)));
        contact.setNumber(phoneCursor.getString(phoneCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)));
        contact.setPhoneType(phoneCursor.getInt(phoneCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.TYPE)));
        contact.setPrimaryNumber(primaryNumber);
        return contact;
    }

    @Override
    @SuppressLint("NewApi")
    public Single<List<Contact>> findAll() {
        return Single.fromCallable(new Callable<List<Contact>>() {
            @Override
            public List<Contact> call() throws Exception {
                try (Cursor contactCursor = contentResolver.query(ContactsContract.Contacts.CONTENT_URI,
                        new String[]{
                                ContactsContract.Contacts._ID,
                                ContactsContract.Contacts.DISPLAY_NAME_PRIMARY,
                                ContactsContract.Contacts.PHOTO_URI,
                                ContactsContract.Contacts.HAS_PHONE_NUMBER
                        },
                        ContactsContract.Contacts.HAS_PHONE_NUMBER + "=?",
                        new String[]{"1"},
                        ContactsContract.Contacts.DISPLAY_NAME + " COLLATE LOCALIZED ASC")) {

                    if (contactCursor != null && contactCursor.getCount() > 0) {
                        List<Contact> contacts = new ArrayList<>(contactCursor.getCount());
                        while (contactCursor.moveToNext()) {
                            long contactId = contactCursor.getLong(contactCursor.getColumnIndex(ContactsContract.Contacts._ID));
                            try (Cursor phoneCursor = contentResolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                                    new String[]{
                                            ContactsContract.CommonDataKinds.Phone._ID,
                                            ContactsContract.CommonDataKinds.Phone.NUMBER,
                                            ContactsContract.CommonDataKinds.Phone.TYPE
                                    },
                                    ContactsContract.CommonDataKinds.Phone.CONTACT_ID + "=?",
                                    new String[]{String.valueOf(contactId)},
                                    null)) {

                                if (phoneCursor != null && phoneCursor.getCount() > 0) {
                                    if (phoneCursor.moveToNext()) {
                                        Contact contact = mapRowToContact(true, contactCursor, phoneCursor);
                                        contacts.add(contact);
                                    }

                                    while (phoneCursor.moveToNext()) {
                                        Contact contact = mapRowToContact(false, contactCursor, phoneCursor);
                                        contacts.add(contact);
                                    }
                                }
                            }
                        }
                        return contacts;
                    } else {
                        return Collections.emptyList();
                    }
                }
            }
        });
    }

}
