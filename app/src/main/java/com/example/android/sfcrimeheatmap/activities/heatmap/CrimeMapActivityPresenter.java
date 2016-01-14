package com.example.android.sfcrimeheatmap.activities.heatmap;

import android.graphics.Color;

import com.example.android.sfcrimeheatmap.R;
import com.example.android.sfcrimeheatmap.helpers.DateHelper;
import com.example.android.sfcrimeheatmap.models.heatmap.CrimeActivityLevel;
import com.example.android.sfcrimeheatmap.models.heatmap.District;
import com.example.android.sfcrimeheatmap.rest.API;
import com.example.android.sfcrimeheatmap.rest.GlobalRestCallback;
import com.example.android.sfcrimeheatmap.rest.models.CrimeIncidentStatistic;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.MarkerOptions;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import retrofit.Call;
import retrofit.Response;
import retrofit.Retrofit;

public class CrimeMapActivityPresenter {
    private static final String DATE_QUERY_STRING = "date > '%s'";
    private static final String SELECT_QUERY_STRING = "pddistrict, count(incidntnum)";
    private static final String GROUP_QUERY_STRING = "pddistrict";

    private final CrimeMapView view;
    private final API api;

    public CrimeMapActivityPresenter(CrimeMapView view, API api) {
        this.view = view;
        this.api = api;
    }

    public void loadMapMarkers() {
        view.showProgressDialog();

        final DateTime oneMonthBeforeToday = DateTime.now().minusMonths(1);

        Call<ArrayList<CrimeIncidentStatistic>> getCrimeDataCall = api.getCrimeData(SELECT_QUERY_STRING,
                buildWhereClause(DateHelper.dateToStringForApi(oneMonthBeforeToday)),
                GROUP_QUERY_STRING);

        getCrimeDataCall.enqueue(new GlobalRestCallback<ArrayList<CrimeIncidentStatistic>>(view) {
            @Override
            public void onResponse(Response<ArrayList<CrimeIncidentStatistic>> response, Retrofit retrofit) {
                view.dismissProgressDialog();
                if (response.isSuccess()) {
                    view.showDate(DateHelper.dateToStringForDisplay(oneMonthBeforeToday));
                    view.showMarkers(processMarkers(response.body()));
                } else {
                    view.showMaterialDialog(R.string.error_fetching_data);
                }
            }
        });
    }

    private String buildWhereClause(String oneMonthBeforeToday) {
        return String.format(DATE_QUERY_STRING, oneMonthBeforeToday);
    }

    private List<MarkerOptions> processMarkers(ArrayList<CrimeIncidentStatistic> incidentStatistics) {
        sortInAscendingOrderOfCrimeReports(incidentStatistics);
        List<MarkerOptions> markerOptionsList = new ArrayList<>();
        int numDistricts = incidentStatistics.size();
        for (int i = 0; i < numDistricts; i++) {
            CrimeIncidentStatistic incidentStatistic = incidentStatistics.get(i);
            District district = District.getDistrict(incidentStatistic.getDistrict());
            if (district != null) {
                if (isHighCrimeArea(numDistricts, i)) {
                    markerOptionsList.add(buildMarker(district, incidentStatistic, CrimeActivityLevel.HIGH));
                } else if (isMediumCrimeArea(numDistricts, i)) {
                    markerOptionsList.add(buildMarker(district, incidentStatistic, CrimeActivityLevel.MEDIUM));
                } else {
                    markerOptionsList.add(buildMarker(district, incidentStatistic, CrimeActivityLevel.LOW));
                }
            }
        }

        return markerOptionsList;
    }

    private boolean isMediumCrimeArea(int numDistricts, int i) {
        return i > numDistricts / 3;
    }

    private boolean isHighCrimeArea(int numDistricts, int i) {
        return i > numDistricts * 2 / 3;
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
        Color.colorToHSV(Color.parseColor(view.getBaseActivityContext().getString(colorResId)), hsv);
        return BitmapDescriptorFactory.defaultMarker(hsv[0]);
    }
}
