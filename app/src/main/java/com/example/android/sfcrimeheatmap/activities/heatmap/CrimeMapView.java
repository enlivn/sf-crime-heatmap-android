package com.example.android.sfcrimeheatmap.activities.heatmap;

import com.example.android.sfcrimeheatmap.activities.BaseActivityCallbacks;
import com.example.android.sfcrimeheatmap.models.heatmap.DistrictModel;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;

public interface CrimeMapView extends BaseActivityCallbacks {
    void showMarkers(List<MarkerOptions> markerOptions);

    void showDate(String dateToDisplay);
}
