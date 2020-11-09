package com.example.medicinelocator;

public class Pharmacy {
    String id;
    String name;
    String phone;
    String address;
    String landmark;
    String mail;
    String gov;
    String city;

    public Pharmacy(String id, String name, String phone, String address, String landmark, String mail, String gov, String city) {
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.address = address;
        this.landmark = landmark;
        this.mail = mail;
        this.gov = gov;
        this.city = city;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getLandmark() {
        return landmark;
    }

    public void setLandmark(String landmark) {
        this.landmark = landmark;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getGov() {
        return gov;
    }

    public void setGov(String gov) {
        this.gov = gov;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }
}
