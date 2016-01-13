package com.example.android.sfcrimeheatmap.rest;

import com.example.android.sfcrimeheatmap.rest.models.CrimeIncident;

import java.util.ArrayList;

import retrofit.Call;
import retrofit.http.GET;
import retrofit.http.Query;

public interface API {

    @GET("/resource/cuks-n6tp.json?")
    Call<ArrayList<CrimeIncident>> getCrimeData(@Query("$where") String startDate);
}