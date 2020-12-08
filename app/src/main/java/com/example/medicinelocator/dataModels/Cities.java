package com.example.medicinelocator.dataModels;

public class Cities {
    private int id;
    private int gov_id;
    private String name_en;
    private String name_ar;

    public Cities(int id, int gov_id, String name_en, String name_ar) {
        this.id = id;
        this.gov_id = gov_id;
        this.name_en = name_en;
        this.name_ar = name_ar;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getGov_id() {
        return gov_id;
    }

    public void setGov_id(int gov_id) {
        this.gov_id = gov_id;
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
