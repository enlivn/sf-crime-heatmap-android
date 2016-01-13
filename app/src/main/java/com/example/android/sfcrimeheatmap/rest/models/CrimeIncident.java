package com.example.android.sfcrimeheatmap.rest.models;

import com.google.android.gms.maps.model.LatLng;
import com.google.gson.annotations.SerializedName;

public class CrimeIncident {
    @SerializedName("address")
    private String address;

    @SerializedName("category")
    private String category;

    @SerializedName("y")
    private Double latitude;

    @SerializedName("x")
    private Double longitude;

    @SerializedName("descript")
    private String description;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LatLng getCoordinates() {
        return new LatLng(getLatitude(), getLongitude());
    }
}
