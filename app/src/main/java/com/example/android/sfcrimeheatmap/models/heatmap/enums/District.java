package com.example.android.sfcrimeheatmap.models.heatmap.enums;

import com.google.android.gms.maps.model.LatLng;

public enum District {
    BAYVIEW(37.730040, -122.384581),
    INGLESIDE(37.720745, -122.454744),
    PARK(37.765262, -122.450094),
    NORTHERN(37.782571, -122.432350),
    CENTRAL(37.794306, -122.407055),
    TARAVAL(37.742392, -122.488166),
    SOUTHERN(37.780723, -122.400281),
    MISSION(37.758517, -122.430431),
    RICHMOND(37.781368, -122.486702),
    TENDERLOIN(37.783743, -122.413761);

    private final Double longitude;
    private final Double latitude;

    District(Double latitude, Double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public static District getDistrict(String name){
        for(District district: values()){
            if(district.toString().equalsIgnoreCase(name)) return district;
        }

        return null;
    }

    public LatLng getCoordinates(){
        return new LatLng(latitude, longitude);
    }
}
