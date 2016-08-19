package com.naya;

import com.naya.coords.UTMcoord;

/**
 * Created by naayadaa on 19.08.16.
 */
public class LineLatLng{
    private UTMcoord p1;
    private UTMcoord p2;
    private UTMcoord basepnt;

    public LineLatLng(UTMcoord p1, UTMcoord p2, UTMcoord basepnt) {
        this.p1 = p1;
        this.p2 = p2;
        this.basepnt = basepnt;
    }

    public UTMcoord getP1() {
        return p1;
    }

    public UTMcoord getP2() {
        return p2;
    }

    public UTMcoord getBasepnt() {
        return basepnt;
    }
}
