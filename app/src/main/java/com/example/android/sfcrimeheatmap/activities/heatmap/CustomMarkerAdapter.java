package com.example.android.sfcrimeheatmap.activities.heatmap;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.example.android.sfcrimeheatmap.R;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

import butterknife.Bind;
import butterknife.ButterKnife;

public class CustomMarkerAdapter implements GoogleMap.InfoWindowAdapter {

    @Bind(R.id.districtName)
    TextView districtNameTv;

    @Bind(R.id.incidentCount)
    TextView incidentCountTv;

    private final View view;

    public CustomMarkerAdapter(Context context) {
        view = LayoutInflater.from(context).inflate(R.layout.adapter_item_customer_marker, null, false);
        ButterKnife.bind(this, view);
    }

    @Override
    public View getInfoWindow(Marker marker) {
        districtNameTv.setText(marker.getTitle());
        incidentCountTv.setText(marker.getSnippet());

        return view;
    }

    @Override
    public View getInfoContents(Marker marker) {
        return null;
    }
}
