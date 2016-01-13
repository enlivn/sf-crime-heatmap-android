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
    private final CrimeMapView view;
    private final API api;

    public CrimeMapActivityPresenter(CrimeMapView view, API api) {
        this.view = view;
        this.api = api;
    }

    public void loadMapMarkers() {
        Call<ArrayList<CrimeIncident>> getCrimeDataCall = api.getCrimeData(view.getBaseActivityContext().getString(R.string.get_crime_data_query_param, DateHelper.getDateOneMonthBefore(view.getBaseActivityContext())));

        getCrimeDataCall.enqueue(new GlobalRestCallback<ArrayList<CrimeIncident>>(view) {
            @Override
            public void onResponse(Response<ArrayList<CrimeIncident>> response, Retrofit retrofit) {
                view.dismissProgressDialog();
                view.showMarkers(response.body());
            }
        });
    }
}
