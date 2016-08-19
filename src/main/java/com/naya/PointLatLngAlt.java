package com.naya;

import com.naya.coords.LatLng;
import com.naya.coords.UTMcoord;

/**
 * Created by naayadaa on 19.08.16.
 */
public class PointLatLngAlt {
    private LatLng latLng;
    private double altitude;

    public PointLatLngAlt(LatLng latLng, double altitude) {
        this.latLng = latLng;
        this.altitude = altitude;
    }

    public PointLatLngAlt(LatLng latLng) {
        this.latLng = latLng;
    }

    public PointLatLngAlt(UTMcoord utmCoord) {
        this.latLng = utmCoord.toLatLng();
    }

    public LatLng getLatLng() {
        return latLng;
    }

    public double getAltitude() {
        return altitude;
    }

    public void setAltitude(double altitude) {
        this.altitude = altitude;
    }
}
