package com.github.aleksandermielczarek.contactpicker.domain;

import android.os.Parcelable;

import org.parceler.Parcel;

/**
 * Created by Aleksander Mielczarek on 03.12.2016.
 */
@Parcel(Parcel.Serialization.BEAN)
public class Contact {

    private long id;
    private String lookupKey;
    private String name;
    private String number;
    private String photo;
    private int numberType;
    private boolean primaryNumber;

    public Contact() {

    }

    public Contact(long id, String lookupKey, String name, String number, String photo, int numberType) {
        this.id = id;
        this.lookupKey = lookupKey;
        this.name = name;
        this.number = number;
        this.photo = photo;
        this.numberType = numberType;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getLookupKey() {
        return lookupKey;
    }

    public void setLookupKey(String lookupKey) {
        this.lookupKey = lookupKey;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public int getNumberType() {
        return numberType;
    }

    public void setNumberType(int numberType) {
        this.numberType = numberType;
    }

    public boolean isPrimaryNumber() {
        return primaryNumber;
    }

    public void setPrimaryNumber(boolean primaryNumber) {
        this.primaryNumber = primaryNumber;
    }
}
