package com.example.android.sfcrimeheatmap.rest.models;

import com.google.gson.annotations.SerializedName;

public class CrimeIncidentStatistic implements Comparable<CrimeIncidentStatistic> {
    @SerializedName("count_incidntnum")
    private Integer incidentCount;

    @SerializedName("pddistrict")
    private String district;

    public String getDistrict() {
        return district;
    }

    public Integer getIncidentCount() {
        return incidentCount;
    }

    @Override
    public int compareTo(CrimeIncidentStatistic another) {
        if (getIncidentCount() < another.getIncidentCount()) return -1;
        else if (getIncidentCount().equals(another.getIncidentCount())) return 0;
        else return 1;
    }
}
