package com.valyakinaleksey.amocrm.models.api;

import com.google.gson.annotations.SerializedName;

public class Lead {
    @SerializedName("id")
    public int id;
    @SerializedName("name")
    public String name;
    @SerializedName("price")
    public int price;
    @SerializedName("status_id")
    public int status_id;
}
