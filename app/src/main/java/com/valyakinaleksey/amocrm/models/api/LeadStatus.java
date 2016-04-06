package com.valyakinaleksey.amocrm.models.api;

import com.google.gson.annotations.SerializedName;

public class LeadStatus {
    @SerializedName("id")
    public int id;
    @SerializedName("name")
    public String name;
    @SerializedName("pipeline_id")
    public int pipeline_id;
    @SerializedName("sort")
    public int sort;
    @SerializedName("color")
    public String color;
    @SerializedName("editable")
    public String editable;


    public LeadStatus() {
    }

    public LeadStatus(int id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof LeadStatus)) return false;

        LeadStatus that = (LeadStatus) o;

        return id == that.id;

    }

    @Override
    public int hashCode() {
        return id;
    }
}
