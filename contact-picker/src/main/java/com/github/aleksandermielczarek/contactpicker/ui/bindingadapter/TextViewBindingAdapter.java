package com.github.aleksandermielczarek.contactpicker.ui.bindingadapter;

import android.content.Context;
import android.databinding.BindingAdapter;
import android.provider.ContactsContract;
import android.util.SparseIntArray;
import android.widget.TextView;

import com.github.aleksandermielczarek.contactpicker.R;
import com.github.aleksandermielczarek.contactpicker.domain.Contact;

/**
 * Created by Aleksander Mielczarek on 03.12.2016.
 */

public class TextViewBindingAdapter {

    private static final SparseIntArray phoneTypes;

    static {
        phoneTypes = new SparseIntArray();
        phoneTypes.put(ContactsContract.CommonDataKinds.Phone.TYPE_ASSISTANT, R.string.type_assistant);
        phoneTypes.put(ContactsContract.CommonDataKinds.Phone.TYPE_CALLBACK, R.string.type_callback);
        phoneTypes.put(ContactsContract.CommonDataKinds.Phone.TYPE_CAR, R.string.type_car);
        phoneTypes.put(ContactsContract.CommonDataKinds.Phone.TYPE_COMPANY_MAIN, R.string.type_company_main);
        phoneTypes.put(ContactsContract.CommonDataKinds.Phone.TYPE_FAX_HOME, R.string.type_fax_home);
        phoneTypes.put(ContactsContract.CommonDataKinds.Phone.TYPE_FAX_WORK, R.string.type_fax_work);
        phoneTypes.put(ContactsContract.CommonDataKinds.Phone.TYPE_HOME, R.string.type_home);
        phoneTypes.put(ContactsContract.CommonDataKinds.Phone.TYPE_ISDN, R.string.type_isdn);
        phoneTypes.put(ContactsContract.CommonDataKinds.Phone.TYPE_MAIN, R.string.type_main);
        phoneTypes.put(ContactsContract.CommonDataKinds.Phone.TYPE_MMS, R.string.type_mms);
        phoneTypes.put(ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE, R.string.type_mobile);
        phoneTypes.put(ContactsContract.CommonDataKinds.Phone.TYPE_OTHER, R.string.type_other);
        phoneTypes.put(ContactsContract.CommonDataKinds.Phone.TYPE_OTHER_FAX, R.string.type_other_fax);
        phoneTypes.put(ContactsContract.CommonDataKinds.Phone.TYPE_PAGER, R.string.type_pager);
        phoneTypes.put(ContactsContract.CommonDataKinds.Phone.TYPE_RADIO, R.string.type_radio);
        phoneTypes.put(ContactsContract.CommonDataKinds.Phone.TYPE_TELEX, R.string.type_telex);
        phoneTypes.put(ContactsContract.CommonDataKinds.Phone.TYPE_TTY_TDD, R.string.type_tty_tdd);
        phoneTypes.put(ContactsContract.CommonDataKinds.Phone.TYPE_WORK, R.string.type_work);
        phoneTypes.put(ContactsContract.CommonDataKinds.Phone.TYPE_WORK_MOBILE, R.string.type_work_mobile);
        phoneTypes.put(ContactsContract.CommonDataKinds.Phone.TYPE_WORK_PAGER, R.string.type_work_pager);
    }

    private TextViewBindingAdapter() {

    }

    @BindingAdapter("contactPhoneTypeAndNumber")
    public static void setPhoneType(TextView textView, Contact contact) {
        if (contact != null) {
            int typeRes = phoneTypes.get(contact.getPhoneType(), ContactsContract.CommonDataKinds.Phone.TYPE_OTHER);
            Context context = textView.getContext();
            String typeAndNumber = context.getString(R.string.contact_type_and_number, context.getString(typeRes), contact.getNumber());
            textView.setText(typeAndNumber);
        }
    }

}
