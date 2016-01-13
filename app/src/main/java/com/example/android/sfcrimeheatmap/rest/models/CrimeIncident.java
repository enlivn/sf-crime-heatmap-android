package com.example.android.sfcrimeheatmap.rest.models;

import com.google.gson.annotations.SerializedName;

public class CrimeIncident {
    @SerializedName("address")
    private String address;

    @SerializedName("category")
    private String category;
}
