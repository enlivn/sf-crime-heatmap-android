package com.example.android.sfcrimeheatmap.activities.heatmap;

import android.graphics.Color;
import android.os.Bundle;

import com.example.android.sfcrimeheatmap.R;
import com.example.android.sfcrimeheatmap.activities.BaseActivity;
import com.example.android.sfcrimeheatmap.activities.MaterialDialogModule;
import com.example.android.sfcrimeheatmap.application.CMAppComponent;
import com.example.android.sfcrimeheatmap.models.heatmap.CrimeActivityLevel;
import com.example.android.sfcrimeheatmap.models.heatmap.District;
import com.example.android.sfcrimeheatmap.rest.models.CrimeIncidentStatistic;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.Collections;

import javax.inject.Inject;

public class CrimeMapActivity extends BaseActivity implements OnMapReadyCallback, CrimeMapView {

    private static final LatLng SOUTH_WEST_SF_BOUNDARY = new LatLng(37.707990, -122.502147);
    private static final LatLng NORTH_EAST_SF_BOUNDARY = new LatLng(37.820354, -122.356578);

    @Inject
    CrimeMapActivityPresenter presenter;

    private GoogleMap mMap;

    @Override
    protected void setupDataInjection(CMAppComponent appComponent) {
        DaggerCrimeMapActivityComponent
                .builder()
                .cMAppComponent(appComponent)
                .crimeMapActivityModule(new CrimeMapActivityModule(this))
                .materialDialogModule(new MaterialDialogModule(this))
                .build()
                .inject(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crime_map);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        initMap();
    }

    private void initMap() {
        int width = getResources().getDisplayMetrics().widthPixels;
        int height = getResources().getDisplayMetrics().heightPixels;
        int padding = (int) (width * 0.12);

        mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(getZoomBounds(), width, height, padding));

        presenter.loadMapMarkers();
    }

    private LatLngBounds getZoomBounds() {
        return new LatLngBounds(SOUTH_WEST_SF_BOUNDARY, NORTH_EAST_SF_BOUNDARY);
    }

    @Override
    public void showMarkers(ArrayList<CrimeIncidentStatistic> incidentStatistics) {
        sortInAscendingOrderOfCrimeReports(incidentStatistics);
        mMap.setInfoWindowAdapter(new CustomMarkerAdapter(this));
        int numDistricts = incidentStatistics.size();
        for (int i = 0; i < numDistricts; i++) {
            CrimeIncidentStatistic incidentStatistic = incidentStatistics.get(i);
            District district = District.getDistrict(incidentStatistic.getDistrict());
            if (district != null) {
                if (i > numDistricts * 2 / 3) {
                    mMap.addMarker(buildMarker(district, incidentStatistic, CrimeActivityLevel.HIGH));
                } else if (i > numDistricts / 3) {
                    mMap.addMarker(buildMarker(district, incidentStatistic, CrimeActivityLevel.MEDIUM));
                } else {
                    mMap.addMarker(buildMarker(district, incidentStatistic, CrimeActivityLevel.LOW));
                }
            }
        }
    }

    private void sortInAscendingOrderOfCrimeReports(ArrayList<CrimeIncidentStatistic> incidentStatistics) {
        Collections.sort(incidentStatistics);
    }

    private MarkerOptions buildMarker(District district,
                                      CrimeIncidentStatistic incidentStatistic,
                                      CrimeActivityLevel crimeActivityLevel) {
        MarkerOptions markerOptions = new MarkerOptions()
                .position(district.getCoordinates())
                .title(incidentStatistic.getDistrict())
                .snippet(String.valueOf(incidentStatistic.getIncidentCount()));

        switch (crimeActivityLevel) {
            case LOW:
                markerOptions.icon(buildMarkerIcon(R.color.lowCrime));
                break;
            case MEDIUM:
                markerOptions.icon(buildMarkerIcon(R.color.mediumCrime));
                break;
            default:
                markerOptions.icon(buildMarkerIcon(R.color.highCrime));
                break;
        }

        return markerOptions;
    }

    private BitmapDescriptor buildMarkerIcon(int colorResId) {
        float[] hsv = new float[3];
        Color.colorToHSV(Color.parseColor(getString(colorResId)), hsv);
        return BitmapDescriptorFactory.defaultMarker(hsv[0]);
    }
}
