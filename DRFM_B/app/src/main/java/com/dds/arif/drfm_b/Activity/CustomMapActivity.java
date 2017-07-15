package com.dds.arif.drfm_b.Activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.appolica.interactiveinfowindow.InfoWindow;
import com.appolica.interactiveinfowindow.InfoWindowManager;
import com.appolica.interactiveinfowindow.fragment.MapInfoWindowFragment;
import com.dds.arif.drfm_b.R;
import com.dds.arif.drfm_b.Fragment.RmgViewFragment;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CustomMapActivity extends AppCompatActivity implements
        GoogleMap.OnMarkerClickListener{

    private static final String RECYCLER_VIEW = "RECYCLER_VIEW_MARKER";
    private static final String FORM_VIEW = "FORM_VIEW_MARKER";
    RmgViewFragment rmgViewFragment;

    public GoogleMap mMap;
    private InfoWindow recyclerWindow;
    private InfoWindow formWindow;
    private InfoWindowManager infoWindowManager;

    Map<Marker,GarmentsInfo> markerGarmentsInfoMap=new HashMap<Marker,GarmentsInfo>();
    ArrayList<GarmentsInfo> garmentsInfos=new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_map);

       /* garmentsInfos.add(new GarmentsInfo("আলিফ গার্মেন্টস লিঃ",new LatLng(23.7688018,90.334449)));
        garmentsInfos.add(new GarmentsInfo("মন্দে অ্যাপারেলস লিমিটেড",new LatLng(23.7688018,90.334449)));
        garmentsInfos.add(new GarmentsInfo("ফ্যালকন গ্রুপ",new LatLng(23.7476117,90.3716493)));
        garmentsInfos.add(new GarmentsInfo("panwin garments",new LatLng(23.7955252,90.3175843)));
        garmentsInfos.add(new GarmentsInfo("OPEX GROUP",new LatLng(23.7955252,90.3175843)));
        garmentsInfos.add(new GarmentsInfo("জিএমএস কম্পোজিট নিটিং ইন্ডাস্ট্রিজ লিঃ -হেড অফিস",new LatLng(23.7955252,90.3175843)));
        garmentsInfos.add(new GarmentsInfo("M-Yew Fashion Ltd.",new LatLng(23.7955252,90.3175843)));
*/
        final MapInfoWindowFragment mapInfoWindowFragment =
                (MapInfoWindowFragment) getSupportFragmentManager().findFragmentById(R.id.infoWindowMap);

        infoWindowManager = mapInfoWindowFragment.infoWindowManager();
        //infoWindowManager.setHideOnFling(true);



        mapInfoWindowFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {


                for (GarmentsInfo garmentsInfo:garmentsInfos
                     ) {
                    final Marker marker1 = googleMap.addMarker(new MarkerOptions().position(garmentsInfo.latLng).snippet(RECYCLER_VIEW));

                    final int offsetX = (int) getResources().getDimension(R.dimen.marker_offset_x);
                    final int offsetY = (int) getResources().getDimension(R.dimen.marker_offset_y);

                    final InfoWindow.MarkerSpecification markerSpec =
                            new InfoWindow.MarkerSpecification(offsetX, offsetY);


                    //recyclerWindow = new InfoWindow(marker1, markerSpec,new RmgViewFragment());
                    garmentsInfo.infoWindow=new InfoWindow(marker1, markerSpec,new RmgViewFragment());
                    markerGarmentsInfoMap.put(marker1,garmentsInfo);
                    googleMap.moveCamera(CameraUpdateFactory.newLatLng(garmentsInfo.latLng));
                }


                googleMap.animateCamera(CameraUpdateFactory.zoomTo(10.0f));
                googleMap.setOnMarkerClickListener(CustomMapActivity.this);
                googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                    @Override
                    public void onMapClick(LatLng latLng) {
                        System.out.println("Working...........");
                    }
                });
            }
        });


        //infoWindowManager.setWindowShowListener(MapFragmentActivity.this);

    }




    @Override
    public boolean onMarkerClick(Marker marker) {


        InfoWindow infoWindow = null;
        switch (marker.getSnippet()) {
            case RECYCLER_VIEW:
                infoWindow = markerGarmentsInfoMap.get(marker).infoWindow;
                break;
            case FORM_VIEW:
                infoWindow = formWindow;
                break;
        }

        if (infoWindow != null) {
            infoWindowManager.toggle(infoWindow, true);
        }

        //((RmgViewFragment)infoWindow.getWindowFragment()).setData(marker.getPosition(),markerGarmentsInfoMap.get(marker).name);

        return true;
    }
}
class GarmentsInfo{

    String name;
    String details;
    LatLng latLng;
    InfoWindow infoWindow;
    public GarmentsInfo(String name, String details, LatLng latLng) {
        this.name = name;
        this.details = details;
        this.latLng = latLng;
    }



}
