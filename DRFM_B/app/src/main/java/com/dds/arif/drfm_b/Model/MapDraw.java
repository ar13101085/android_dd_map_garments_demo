package com.dds.arif.drfm_b.Model;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;

/**
 * Created by Arif on 7/10/2017.
 */

public class MapDraw {
    public int priority;
    public LatLng latLng;
    public Marker marker;

    public MapDraw(int priority, LatLng latLng) {
        this.priority = priority;
        this.latLng = latLng;
    }

    public MapDraw(int priority, LatLng latLng, Marker marker) {
        this.priority = priority;
        this.latLng = latLng;
        this.marker = marker;
    }
}
