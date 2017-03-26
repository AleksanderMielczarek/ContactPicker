package com.github.aleksandermielczarek.contactpicker.ui.bindingadapter;

import android.content.Context;
import android.databinding.BindingAdapter;
import android.provider.ContactsContract;
import android.util.SparseIntArray;
import android.widget.TextView;

import com.github.aleksandermielczarek.contactpicker.R;
import com.github.aleksandermielczarek.contactpicker.domain.data.Contact;
import com.github.aleksandermielczarek.contactpicker.util.Utils;

/**
 * Created by Aleksander Mielczarek on 03.12.2016.
 */

public final class TextViewBindingAdapter {

    private static final SparseIntArray phoneStringTypes;
    private static final SparseIntArray phoneAttrTypes;

    static {
        phoneStringTypes = new SparseIntArray();
        phoneStringTypes.put(ContactsContract.CommonDataKinds.Phone.TYPE_ASSISTANT, R.string.contact_picker_number_type_assistant);
        phoneStringTypes.put(ContactsContract.CommonDataKinds.Phone.TYPE_CALLBACK, R.string.contact_picker_number_type_callback);
        phoneStringTypes.put(ContactsContract.CommonDataKinds.Phone.TYPE_CAR, R.string.contact_picker_number_type_car);
        phoneStringTypes.put(ContactsContract.CommonDataKinds.Phone.TYPE_COMPANY_MAIN, R.string.contact_picker_number_type_company_main);
        phoneStringTypes.put(ContactsContract.CommonDataKinds.Phone.TYPE_FAX_HOME, R.string.contact_picker_number_type_fax_home);
        phoneStringTypes.put(ContactsContract.CommonDataKinds.Phone.TYPE_FAX_WORK, R.string.contact_picker_number_type_fax_work);
        phoneStringTypes.put(ContactsContract.CommonDataKinds.Phone.TYPE_HOME, R.string.contact_picker_number_type_home);
        phoneStringTypes.put(ContactsContract.CommonDataKinds.Phone.TYPE_ISDN, R.string.contact_picker_number_type_isdn);
        phoneStringTypes.put(ContactsContract.CommonDataKinds.Phone.TYPE_MAIN, R.string.contact_picker_number_type_main);
        phoneStringTypes.put(ContactsContract.CommonDataKinds.Phone.TYPE_MMS, R.string.contact_picker_number_type_mms);
        phoneStringTypes.put(ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE, R.string.contact_picker_number_type_mobile);
        phoneStringTypes.put(ContactsContract.CommonDataKinds.Phone.TYPE_OTHER, R.string.contact_picker_number_type_other);
        phoneStringTypes.put(ContactsContract.CommonDataKinds.Phone.TYPE_OTHER_FAX, R.string.contact_picker_number_type_other_fax);
        phoneStringTypes.put(ContactsContract.CommonDataKinds.Phone.TYPE_PAGER, R.string.contact_picker_number_type_pager);
        phoneStringTypes.put(ContactsContract.CommonDataKinds.Phone.TYPE_RADIO, R.string.contact_picker_number_type_radio);
        phoneStringTypes.put(ContactsContract.CommonDataKinds.Phone.TYPE_TELEX, R.string.contact_picker_number_type_telex);
        phoneStringTypes.put(ContactsContract.CommonDataKinds.Phone.TYPE_TTY_TDD, R.string.contact_picker_number_type_tty_tdd);
        phoneStringTypes.put(ContactsContract.CommonDataKinds.Phone.TYPE_WORK, R.string.contact_picker_number_type_work);
        phoneStringTypes.put(ContactsContract.CommonDataKinds.Phone.TYPE_WORK_MOBILE, R.string.contact_picker_number_type_work_mobile);
        phoneStringTypes.put(ContactsContract.CommonDataKinds.Phone.TYPE_WORK_PAGER, R.string.contact_picker_number_type_work_pager);

        phoneAttrTypes = new SparseIntArray();
        phoneAttrTypes.put(ContactsContract.CommonDataKinds.Phone.TYPE_ASSISTANT, R.attr.contactPickerNumberTypeAssistant);
        phoneAttrTypes.put(ContactsContract.CommonDataKinds.Phone.TYPE_CALLBACK, R.attr.contactPickerNumberTypeCallback);
        phoneAttrTypes.put(ContactsContract.CommonDataKinds.Phone.TYPE_CAR, R.attr.contactPickerNumberTypeCar);
        phoneAttrTypes.put(ContactsContract.CommonDataKinds.Phone.TYPE_COMPANY_MAIN, R.attr.contactPickerNumberTypeCompanyMain);
        phoneAttrTypes.put(ContactsContract.CommonDataKinds.Phone.TYPE_FAX_HOME, R.attr.contactPickerNumberTypeFaxHome);
        phoneAttrTypes.put(ContactsContract.CommonDataKinds.Phone.TYPE_FAX_WORK, R.attr.contactPickerNumberTypeFaxWork);
        phoneAttrTypes.put(ContactsContract.CommonDataKinds.Phone.TYPE_HOME, R.attr.contactPickerNumberTypeHome);
        phoneAttrTypes.put(ContactsContract.CommonDataKinds.Phone.TYPE_ISDN, R.attr.contactPickerNumberTypeIsdn);
        phoneAttrTypes.put(ContactsContract.CommonDataKinds.Phone.TYPE_MAIN, R.attr.contactPickerNumberTypeMain);
        phoneAttrTypes.put(ContactsContract.CommonDataKinds.Phone.TYPE_MMS, R.attr.contactPickerNumberTypeMms);
        phoneAttrTypes.put(ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE, R.attr.contactPickerNumberTypeMobile);
        phoneAttrTypes.put(ContactsContract.CommonDataKinds.Phone.TYPE_OTHER, R.attr.contactPickerNumberTypeOther);
        phoneAttrTypes.put(ContactsContract.CommonDataKinds.Phone.TYPE_OTHER_FAX, R.attr.contactPickerNumberTypeOtherFax);
        phoneAttrTypes.put(ContactsContract.CommonDataKinds.Phone.TYPE_PAGER, R.attr.contactPickerNumberTypePager);
        phoneAttrTypes.put(ContactsContract.CommonDataKinds.Phone.TYPE_RADIO, R.attr.contactPickerNumberTypeRadio);
        phoneAttrTypes.put(ContactsContract.CommonDataKinds.Phone.TYPE_TELEX, R.attr.contactPickerNumberTypeTelex);
        phoneAttrTypes.put(ContactsContract.CommonDataKinds.Phone.TYPE_TTY_TDD, R.attr.contactPickerNumberTypeTtyTdd);
        phoneAttrTypes.put(ContactsContract.CommonDataKinds.Phone.TYPE_WORK, R.attr.contactPickerNumberTypeWork);
        phoneAttrTypes.put(ContactsContract.CommonDataKinds.Phone.TYPE_WORK_MOBILE, R.attr.contactPickerNumberTypeWorkMobile);
        phoneAttrTypes.put(ContactsContract.CommonDataKinds.Phone.TYPE_WORK_PAGER, R.attr.contactPickerNumberTypeWorkPager);
    }

    private TextViewBindingAdapter() {

    }

    @BindingAdapter("contactPhoneTypeAndNumber")
    public static void setPhoneType(TextView textView, Contact contact) {
        if (contact != null) {
            int stringTypeRes = phoneStringTypes.get(contact.getPhoneType(), ContactsContract.CommonDataKinds.Phone.TYPE_OTHER);
            int attrTypeRes = phoneAttrTypes.get(contact.getPhoneType(), ContactsContract.CommonDataKinds.Phone.TYPE_OTHER);
            Context context = textView.getContext();
            int typeRes = Utils.stringFromAttr(context, attrTypeRes, stringTypeRes);
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
