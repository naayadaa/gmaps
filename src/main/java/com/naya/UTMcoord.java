package com.naya;


/**
 * Created by naayadaa on 15.08.16.
 */
public class UTMcoord {

    private int utmZone;
    private String utmLatZone;
    private Double easting;
    private Double northing;

    private String southernHemisphere = "ACDEFGHJKLM";

    public UTMcoord(int utmZone, String utmLatZone, double easting, double northing) {
        this.utmZone = utmZone;
        this.utmLatZone = utmLatZone;
        this.easting = easting;
        this.northing = northing;
    }

    public String getHemisphere()
    {
        String hemisphere = "N";
        if (southernHemisphere.indexOf(utmLatZone) > -1)
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

    @Override
    public String toString() {
        return utmZone + utmLatZone + " " +
                easting.intValue() + " " + northing.intValue();
    }
}
