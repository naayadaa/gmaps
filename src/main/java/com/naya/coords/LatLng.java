package com.naya.coords;

/**
 * Created by naayadaa on 17.08.16.
 */
public class LatLng {
    private Double lat;
    private Double lng;
    private static final LatLng2UTMConverter converter = new LatLng2UTMConverter();

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

    public UTMcoord toUTM(){
        return converter.convert(this);
    }

    @Override
    public String toString() {
        return lat + " " + lng;
    }
}
