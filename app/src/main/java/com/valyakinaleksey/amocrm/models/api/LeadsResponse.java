package com.valyakinaleksey.amocrm.models.api;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class LeadsResponse {
    @SerializedName("leads")
    public List<Lead> leads;
    @SerializedName("server_time")
    public int server_time;
}
