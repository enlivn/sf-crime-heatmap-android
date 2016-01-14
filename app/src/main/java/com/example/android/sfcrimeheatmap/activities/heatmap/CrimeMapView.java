package com.example.android.sfcrimeheatmap.activities.heatmap;

import com.example.android.sfcrimeheatmap.activities.BaseActivityCallbacks;
import com.example.android.sfcrimeheatmap.rest.models.CrimeIncidentStatistic;

import java.util.ArrayList;

public interface CrimeMapView extends BaseActivityCallbacks {
    void showMarkers(ArrayList<CrimeIncidentStatistic> body);
}
