package com.example.android.sfcrimeheatmap.activities.heatmap;

import com.example.android.sfcrimeheatmap.R;
import com.example.android.sfcrimeheatmap.helpers.DateHelper;
import com.example.android.sfcrimeheatmap.rest.API;
import com.example.android.sfcrimeheatmap.rest.GlobalRestCallback;
import com.example.android.sfcrimeheatmap.rest.models.CrimeIncident;

import java.util.ArrayList;

import retrofit.Call;
import retrofit.Response;
import retrofit.Retrofit;

public class CrimeMapActivityPresenter {
    private static final String DATE_QUERY_STRING = "date > '%s'";
    private final CrimeMapView view;
    private final API api;

    public CrimeMapActivityPresenter(CrimeMapView view, API api) {
        this.view = view;
        this.api = api;
    }

    public void loadMapMarkers() {
        view.showProgressDialog();
        Call<ArrayList<CrimeIncident>> getCrimeDataCall = api.getCrimeData(String.format(DATE_QUERY_STRING,
                DateHelper.getDateOneMonthBefore(view.getBaseActivityContext())));

        getCrimeDataCall.enqueue(new GlobalRestCallback<ArrayList<CrimeIncident>>(view) {
            @Override
            public void onResponse(Response<ArrayList<CrimeIncident>> response, Retrofit retrofit) {
                view.dismissProgressDialog();
                if (response.isSuccess()) {
                    view.showMarkers(response.body());
                } else {
                    view.showMaterialDialog(R.string.error_fetching_data);
                }
            }
        });
    }
}
