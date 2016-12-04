package com.github.aleksandermielczarek.contactpicker.domain;

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
    private int phoneType;
    private boolean primaryNumber;

    public Contact() {

    }

    public Contact(long id, String lookupKey, String name, String number, String photo, int phoneType) {
        this.id = id;
        this.lookupKey = lookupKey;
        this.name = name;
        this.number = number;
        this.photo = photo;
        this.phoneType = phoneType;
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

    public int getPhoneType() {
        return phoneType;
    }

    public void setPhoneType(int phoneType) {
        this.phoneType = phoneType;
    }

    public boolean isPrimaryNumber() {
        return primaryNumber;
    }

    public void setPrimaryNumber(boolean primaryNumber) {
        this.primaryNumber = primaryNumber;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Contact contact = (Contact) o;

        if (id != contact.id) return false;
        if (phoneType != contact.phoneType) return false;
        if (!lookupKey.equals(contact.lookupKey)) return false;
        if (!name.equals(contact.name)) return false;
        if (!number.equals(contact.number)) return false;
        return photo != null ? photo.equals(contact.photo) : contact.photo == null;

    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + lookupKey.hashCode();
        result = 31 * result + name.hashCode();
        result = 31 * result + number.hashCode();
        result = 31 * result + (photo != null ? photo.hashCode() : 0);
        result = 31 * result + phoneType;
        return result;
    }
}
