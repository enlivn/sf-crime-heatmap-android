package com.example.android.sfcrimeheatmap.activities.heatmap;

import android.graphics.Color;

import com.example.android.sfcrimeheatmap.R;
import com.example.android.sfcrimeheatmap.helpers.DateHelper;
import com.example.android.sfcrimeheatmap.models.heatmap.CrimeActivityLevel;
import com.example.android.sfcrimeheatmap.models.heatmap.DistrictModel;
import com.example.android.sfcrimeheatmap.models.heatmap.enums.District;
import com.example.android.sfcrimeheatmap.rest.API;
import com.example.android.sfcrimeheatmap.rest.models.CrimeIncidentStatistic;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.VisibleRegion;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class CrimeMapActivityPresenter {
    private static final String WHERE_DATE_QUERY_STRING = "date > '%s'";
    private static final String WHERE_DISTRICT_QUERY_STRING = "pddistrict = '%s'";
    private static final String SELECT_QUERY_STRING = "pddistrict, count(incidntnum)";
    private static final String GROUP_QUERY_STRING = "pddistrict";

    private static final Integer LIMIT_PER_API_CALL = 3;
    private static final Integer MAX_PAGES_OF_INCIDENTS_TO_FETCH_PER_DISTRICT = 5;

    private final CrimeMapView view;
    private final API api;
    private final DateTime oneMonthBeforeToday;

    public CrimeMapActivityPresenter(CrimeMapView view, API api) {
        oneMonthBeforeToday = DateTime.now().minusMonths(1);
        this.view = view;
        this.api = api;
    }

    public void loadPaginatedCrimeMarkersForDistrictsOnScreen(List<DistrictModel> districtModels, VisibleRegion visibleRegion) {
        view.showProgressDialog();

        rx.Observable
                .from(districtModels)
                .subscribeOn(Schedulers.io())
                .filter(this::getDistrictModelsWithPagesLeftToFetch)
                .filter(districtModel -> getDistrictModelsOnScreen(districtModel, visibleRegion))
                .flatMap(this::fetchCrimeIncidentsForDistrict)
                .observeOn(AndroidSchedulers.mainThread())
                .doOnTerminate(this::dismissProgressDialog)
                .doOnError(this::showErrorDialog)
                .onErrorResumeNext(throwable -> Observable.empty())
                .subscribe(crimeIncidentStatistics -> view.showMarkers(processCrimeIncidentMarkers(crimeIncidentStatistics)));
    }

    private void dismissProgressDialog() {
        view.dismissProgressDialog();
    }

    private Boolean getDistrictModelsWithPagesLeftToFetch(DistrictModel districtModel) {
        return districtModel.getApiPage() < MAX_PAGES_OF_INCIDENTS_TO_FETCH_PER_DISTRICT;
    }

    private Boolean getDistrictModelsOnScreen(DistrictModel districtModel, VisibleRegion visibleRegion) {
        return isDistrictOnScreen(districtModel, visibleRegion);
    }

    private boolean isDistrictOnScreen(DistrictModel districtModel, VisibleRegion visibleRegion) {
        return visibleRegion.latLngBounds.contains(districtModel.getDistrict().getCoordinates());
    }

    private void incrementPagination(DistrictModel districtModel) {
        districtModel.setApiPage(districtModel.getApiPage() + 1);
    }

    private Observable<ArrayList<CrimeIncidentStatistic>> fetchCrimeIncidentsForDistrict(DistrictModel districtModel) {
        return api.getCrimeIncidentsPerDistrict(buildWhereDistrictAndDateClause(districtModel.getDistrict().toString()),
                districtModel.getApiPage(),
                LIMIT_PER_API_CALL)
                .doOnNext(results -> incrementPagination(districtModel));
    }

    private List<MarkerOptions> processCrimeIncidentMarkers(ArrayList<CrimeIncidentStatistic> crimeIncidentStatistics) {
        List<MarkerOptions> markerOptionsList = new ArrayList<>();
        for (CrimeIncidentStatistic crimeIncidentStatistic : crimeIncidentStatistics) {
            markerOptionsList.add(
                    buildMarkerOption(crimeIncidentStatistic.getCoordinates(),
                            crimeIncidentStatistic.getDescription(),
                            crimeIncidentStatistic.getAddress()));
        }

        return markerOptionsList;
    }

    public void loadDistrictCountMarkers() {
        view.showProgressDialog();

        getCrimeCountsPerDistrict()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnTerminate(this::dismissProgressDialog)
                .doOnError(this::showErrorDialog)
                .onErrorResumeNext(throwable -> Observable.empty())
                .subscribe(this::processCrimeIncidentStatistics);
    }

    private void showErrorDialog(Throwable throwable) {
        view.showMaterialDialog(R.string.error_fetching_data);
    }

    private Observable<ArrayList<CrimeIncidentStatistic>> getCrimeCountsPerDistrict() {
        return api.getCrimeCountsPerDistrict(SELECT_QUERY_STRING,
                buildWhereDateClause(),
                GROUP_QUERY_STRING);
    }

    private void processCrimeIncidentStatistics(ArrayList<CrimeIncidentStatistic> crimeIncidentStatistics) {
        view.showDate(DateHelper.dateToStringForDisplay(oneMonthBeforeToday));
        view.showMarkers(processCrimeCountMarkers(crimeIncidentStatistics));
    }

    private String buildWhereDistrictAndDateClause(String districtName) {
        return buildWhereDateClause() + " and " + buildWhereDistrictClause(districtName);
    }

    private String buildWhereDistrictClause(String districtName) {
        return String.format(WHERE_DISTRICT_QUERY_STRING, districtName);
    }

    private String buildWhereDateClause() {
        return String.format(WHERE_DATE_QUERY_STRING, DateHelper.dateToStringForApi(oneMonthBeforeToday));
    }

    private List<MarkerOptions> processCrimeCountMarkers(ArrayList<CrimeIncidentStatistic> incidentStatistics) {
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

    private void sortInAscendingOrderOfCrimeReports(ArrayList<CrimeIncidentStatistic> incidentStatistics) {
        Collections.sort(incidentStatistics);
    }

    private boolean isHighCrimeArea(int numDistricts, int i) {
        return i > numDistricts * 2 / 3;
    }

    private boolean isMediumCrimeArea(int numDistricts, int i) {
        return i > numDistricts / 3;
    }

    private MarkerOptions buildMarker(District district,
                                      CrimeIncidentStatistic incidentStatistic,
                                      CrimeActivityLevel crimeActivityLevel) {
        MarkerOptions markerOptions = buildMarkerOption(district.getCoordinates(), incidentStatistic.getDistrict(), String.valueOf(incidentStatistic.getIncidentCount()));

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

    private MarkerOptions buildMarkerOption(LatLng position, String title, String snippet) {
        return new MarkerOptions()
                .position(position)
                .title(title)
                .snippet(snippet);
    }

    private BitmapDescriptor buildMarkerIcon(int colorResId) {
        float[] hsv = new float[3];
        Color.colorToHSV(Color.parseColor(view.getBaseActivityContext().getString(colorResId)), hsv);
        return BitmapDescriptorFactory.defaultMarker(hsv[0]);
    }
}
