package com.example.android.sfcrimeheatmap.rest.models;

import com.google.android.gms.maps.model.LatLng;
import com.google.gson.annotations.SerializedName;

public class CrimeIncidentStatistic implements Comparable<CrimeIncidentStatistic> {
    @SerializedName("count_incidntnum")
    private Integer incidentCount;

    @SerializedName("pddistrict")
    private String district;

    @SerializedName("x")
    private Double longitude;

    @SerializedName("y")
    private Double latitude;

    @SerializedName("descript")
    private String description;

    @SerializedName("address")
    private String address;

    public String getDistrict() {
        return district;
    }

    public Integer getIncidentCount() {
        return incidentCount;
    }

    public String getAddress() {
        return address;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public int compareTo(CrimeIncidentStatistic another) {
        if (getIncidentCount() < another.getIncidentCount()) return -1;
        else if (getIncidentCount().equals(another.getIncidentCount())) return 0;
        else return 1;
    }

    public LatLng getCoordinates(){
        return new LatLng(latitude, longitude);
    }
}
