package com.example.medicinelocator.dataModels;

public class Medicine {

    String id;
    String name_en;
    String name_ar;
    String price;
    String availability;

    public Medicine(String id, String name_en, String name_ar, String price, String availability) {
        this.id = id;
        this.name_en = name_en;
        this.name_ar = name_ar;
        this.price = price;
        this.availability = availability;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName_en() {
        return name_en;
    }

    public void setName_en(String name_en) {
        this.name_en = name_en;
    }

    public String getName_ar() {
        return name_ar;
    }

    public void setName_ar(String name_ar) {
        this.name_ar = name_ar;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getAvailability() {
        return availability;
    }

    public void setAvailability(String availability) {
        this.availability = availability;
    }
}
