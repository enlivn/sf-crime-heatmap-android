package com.example.android.sfcrimeheatmap.models.heatmap;

public class DistrictModel {
    private com.example.android.sfcrimeheatmap.models.heatmap.enums.District district;

    private int apiPage;

    public DistrictModel(com.example.android.sfcrimeheatmap.models.heatmap.enums.District district) {
        this.district = district;
        this.apiPage = 0;
    }

    public com.example.android.sfcrimeheatmap.models.heatmap.enums.District getDistrict() {
        return district;
    }

    public int getApiPage() {
        return apiPage;
    }

    public void setApiPage(int apiPage) {
        this.apiPage = apiPage;
    }
}
