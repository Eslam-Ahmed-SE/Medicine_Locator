package com.example.medicinelocator.dataModels;

public class Govs {
    private int id;
    private String name_en;
    private String name_ar;


    public Govs(int id, String name_en, String name_ar) {
        this.id = id;
        this.name_en = name_en;
        this.name_ar = name_ar;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
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
}
