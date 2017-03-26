package com.github.aleksandermielczarek.contactpicker.ui.bindingadapter;

import android.content.Context;
import android.databinding.BindingAdapter;
import android.provider.ContactsContract;
import android.util.SparseIntArray;
import android.widget.TextView;

import com.github.aleksandermielczarek.contactpicker.R;
import com.github.aleksandermielczarek.contactpicker.domain.data.Contact;

/**
 * Created by Aleksander Mielczarek on 03.12.2016.
 */

public final class TextViewBindingAdapter {

    private static final SparseIntArray phoneTypes;

    static {
        phoneTypes = new SparseIntArray();
        phoneTypes.put(ContactsContract.CommonDataKinds.Phone.TYPE_ASSISTANT, R.string.contact_picker_number_type_assistant);
        phoneTypes.put(ContactsContract.CommonDataKinds.Phone.TYPE_CALLBACK, R.string.contact_picker_number_type_callback);
        phoneTypes.put(ContactsContract.CommonDataKinds.Phone.TYPE_CAR, R.string.contact_picker_number_type_car);
        phoneTypes.put(ContactsContract.CommonDataKinds.Phone.TYPE_COMPANY_MAIN, R.string.contact_picker_number_type_company_main);
        phoneTypes.put(ContactsContract.CommonDataKinds.Phone.TYPE_FAX_HOME, R.string.contact_picker_number_type_fax_home);
        phoneTypes.put(ContactsContract.CommonDataKinds.Phone.TYPE_FAX_WORK, R.string.contact_picker_number_type_fax_work);
        phoneTypes.put(ContactsContract.CommonDataKinds.Phone.TYPE_HOME, R.string.contact_picker_number_type_home);
        phoneTypes.put(ContactsContract.CommonDataKinds.Phone.TYPE_ISDN, R.string.contact_picker_number_type_isdn);
        phoneTypes.put(ContactsContract.CommonDataKinds.Phone.TYPE_MAIN, R.string.contact_picker_number_type_main);
        phoneTypes.put(ContactsContract.CommonDataKinds.Phone.TYPE_MMS, R.string.contact_picker_number_type_mms);
        phoneTypes.put(ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE, R.string.contact_picker_number_type_mobile);
        phoneTypes.put(ContactsContract.CommonDataKinds.Phone.TYPE_OTHER, R.string.contact_picker_number_type_other);
        phoneTypes.put(ContactsContract.CommonDataKinds.Phone.TYPE_OTHER_FAX, R.string.contact_picker_number_type_other_fax);
        phoneTypes.put(ContactsContract.CommonDataKinds.Phone.TYPE_PAGER, R.string.contact_picker_number_type_pager);
        phoneTypes.put(ContactsContract.CommonDataKinds.Phone.TYPE_RADIO, R.string.contact_picker_number_type_radio);
        phoneTypes.put(ContactsContract.CommonDataKinds.Phone.TYPE_TELEX, R.string.contact_picker_number_type_telex);
        phoneTypes.put(ContactsContract.CommonDataKinds.Phone.TYPE_TTY_TDD, R.string.contact_picker_number_type_tty_tdd);
        phoneTypes.put(ContactsContract.CommonDataKinds.Phone.TYPE_WORK, R.string.contact_picker_number_type_work);
        phoneTypes.put(ContactsContract.CommonDataKinds.Phone.TYPE_WORK_MOBILE, R.string.contact_picker_number_type_work_mobile);
        phoneTypes.put(ContactsContract.CommonDataKinds.Phone.TYPE_WORK_PAGER, R.string.contact_picker_number_type_work_pager);
    }

    private TextViewBindingAdapter() {

    }

    @BindingAdapter("contactPhoneTypeAndNumber")
    public static void setPhoneType(TextView textView, Contact contact) {
        if (contact != null) {
            int typeRes = phoneTypes.get(contact.getPhoneType(), ContactsContract.CommonDataKinds.Phone.TYPE_OTHER);
            Context context = textView.getContext();
            String typeAndNumber = context.getString(R.string.contact_picker_contact_type_and_number, context.getString(typeRes), contact.getNumber());
            textView.setText(typeAndNumber);
        }
    }

    @BindingAdapter("number")
    public static void setContactCounter(TextView textView, Number number) {
        if (number != null) {
            String counterText = String.valueOf(number);
            textView.setText(counterText);
        }
    }

}
