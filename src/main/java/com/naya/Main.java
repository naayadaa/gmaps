package com.naya;

import com.naya.coords.LatLng;
import com.naya.coords.LatLng2UTMConverter;
import com.naya.coords.UTM2LatLngConverter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by naayadaa on 17.08.16.
 */
public class Main {


    public static void main(String[] args) {
        /*LatLng2UTMConverter latLng2UTMCovertor = new LatLng2UTMConverter();
        UTM2LatLngConverter utm2LatLngConvertor = new UTM2LatLngConverter();

        List<LatLng> latLngs = new ArrayList<>();

      *//*  LatLng latLng1 = new LatLng( 0.0000,  0.0000  );
        LatLng latLng2 = new LatLng(0.1300 ,  -0.2324);
        LatLng latLng3 = new LatLng(-12.7650,  -33.8765);
        LatLng latLng4 = new LatLng(-80.5434 , -170.6540);
        LatLng latLng5 = new LatLng(90.0000 ,  177.0000);
        LatLng latLng6 = new LatLng(-90.0000 , -177.0000);
        LatLng latLng7 = new LatLng(90.0000 ,   3.0000);*//*

        latLngs.add(new LatLng( 0.0000,  0.0000  ));
        latLngs.add(new LatLng(0.1300 ,  -0.2324));
        latLngs.add(new LatLng(-12.7650,  -33.8765));
        latLngs.add(new LatLng(-80.5434 , -170.6540));
        latLngs.add(new LatLng(90.0000 ,  177.0000));
        latLngs.add(new LatLng(-90.0000 , -177.0000));
        latLngs.add(new LatLng(90.0000 ,   3.0000));

        for (LatLng latLng : latLngs){
            System.out.println(latLng.toString() + "          " + latLng2UTMCovertor.convert(latLng));
            System.out.println(utm2LatLngConvertor.convert(latLng2UTMCovertor.convert(latLng)).toString());
        }
*/
    }
}
