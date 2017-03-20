package com.github.aleksandermielczarek.contactpicker.domain.data;

import org.parceler.Parcel;

/**
 * Created by Aleksander Mielczarek on 03.12.2016.
 */
@Parcel(Parcel.Serialization.BEAN)
public class Contact {

    private long id;
    private String name;
    private String photo;
    private String number;
    private int phoneType;
    private boolean primaryNumber;

    public Contact() {

    }

    public Contact(long id,  String name, String photo, String number, int phoneType, boolean primaryNumber) {
        this.id = id;
        this.name = name;
        this.photo = photo;
        this.number = number;
        this.phoneType = phoneType;
        this.primaryNumber = primaryNumber;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id =id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
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
        if (primaryNumber != contact.primaryNumber) return false;
        if (name != null ? !name.equals(contact.name) : contact.name != null) return false;
        if (photo != null ? !photo.equals(contact.photo) : contact.photo != null) return false;
        return number != null ? number.equals(contact.number) : contact.number == null;

    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (photo != null ? photo.hashCode() : 0);
        result = 31 * result + (number != null ? number.hashCode() : 0);
        result = 31 * result + phoneType;
        result = 31 * result + (primaryNumber ? 1 : 0);
        return result;
    }
}
