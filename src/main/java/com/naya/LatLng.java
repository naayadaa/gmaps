package com.naya;

/**
 * Created by naayadaa on 17.08.16.
 */
public class LatLng {
    private Double lat;
    private Double lng;

    public LatLng(Double lat, Double lng) {
        this.lat = lat;
        this.lng = lng;
    }

    public Double getLat() {
        return lat;
    }

    public Double getLng() {
        return lng;
    }

    @Override
    public String toString() {
        return lat + " " + lng;
    }
}
