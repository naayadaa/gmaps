package com.naya.coords;


/**
 * Created by naayadaa on 15.08.16.
 */
public class UTMcoord {

    private int utmZone;
    private String utmLatZone;
    private Double easting;
    private Double northing;

    private String southernHemisphere = "ACDEFGHJKLM";

    private static final UTM2LatLngConverter converter = new UTM2LatLngConverter();

    public static UTMcoord getZero(){
        return new LatLng(0., 0.).toUTM();
    }
    public UTMcoord(int utmZone, String utmLatZone, double easting, double northing) {
        this.utmZone = utmZone;
        this.utmLatZone = utmLatZone;
        this.easting = easting;
        this.northing = northing;
    }

    public String getHemisphere()
    {
        String hemisphere = "N";
        if (southernHemisphere.contains(utmLatZone))
        {
            hemisphere = "S";
        }
        return hemisphere;
    }

    public int getUtmZone() {
        return utmZone;
    }

    public String getUtmLatZone() {
        return utmLatZone;
    }

    public double getEasting() {
        return easting;
    }

    public double getNorthing() {
        return northing;
    }

    public LatLng toLatLng(){
        return converter.convert(this);
    }

    public boolean isZero(){
        LatLng latLng = this.toLatLng();
        return latLng.getLat().equals(0.) && latLng.getLng().equals(0.);
    }

    @Override
    public String toString() {
        return utmZone + utmLatZone + " " +
                easting.intValue() + " " + northing.intValue();
    }

    public double getDistance(UTMcoord newUTMcoord) {
        return Math.sqrt(Math.pow(Math.abs(easting - newUTMcoord.getEasting()), 2) +
                Math.pow(Math.abs(northing - newUTMcoord.getNorthing()), 2));
    }

}
