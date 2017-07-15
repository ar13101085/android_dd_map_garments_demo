package com.dds.arif.drfm_b.Activity;

import android.app.ActionBar;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.appolica.interactiveinfowindow.InfoWindow;
import com.appolica.interactiveinfowindow.InfoWindowManager;
import com.appolica.interactiveinfowindow.fragment.MapInfoWindowFragment;
import com.dds.arif.drfm_b.Fragment.NearDetailsFragment;
import com.dds.arif.drfm_b.Model.Nearest;
import com.dds.arif.drfm_b.Model.Result;
import com.dds.arif.drfm_b.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.MySSLSocketFactory;

import org.json.JSONArray;
import org.json.JSONObject;

import java.security.KeyStore;
import java.util.HashMap;

import cz.msebera.android.httpclient.Header;

public class NearestLocationActivity extends AppCompatActivity implements
        GoogleMap.OnMarkerClickListener {


    private static final String RECYCLER_VIEW = "RECYCLER_VIEW_MARKER";
    private static final String FORM_VIEW = "FORM_VIEW_MARKER";

    private InfoWindowManager infoWindowManager;
    GoogleMap map;

    LatLng latLng;
    String type;
    LatLng garmentsLatlng;

    private InfoWindow recyclerWindow;

    HashMap<Marker,InfoMap> allResult=new HashMap<Marker,InfoMap>();
    BitmapDescriptor bitmapDescriptor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_map);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        final MapInfoWindowFragment mapInfoWindowFragment =
                (MapInfoWindowFragment) getSupportFragmentManager().findFragmentById(R.id.infoWindowMap);

        infoWindowManager = mapInfoWindowFragment.infoWindowManager();
        //infoWindowManager.setHideOnFling(true);


        latLng=new LatLng(getIntent().getDoubleExtra("lat",0),getIntent().getDoubleExtra("lng",0));
        type=getIntent().getStringExtra("type");
        if(type.equalsIgnoreCase("hospital")){
            bitmapDescriptor=BitmapDescriptorFactory.fromResource(R.mipmap.ic_hospital_markar);
        }else{
            bitmapDescriptor=BitmapDescriptorFactory.fromResource(R.mipmap.ic_fire_station);
        }

        mapInfoWindowFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                map=googleMap;
                map.addMarker(new MarkerOptions().position(latLng).snippet("hospital").icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_garments_markar)));
                loadMap();
                googleMap.setOnMarkerClickListener(NearestLocationActivity.this);
            }
        });
    }

    @Override
    public boolean onMarkerClick(Marker marker) {

        if (allResult.get(marker) != null) {
            infoWindowManager.toggle(allResult.get(marker).infoWindow, true);
        }else{
            return false;
        }

        ((NearDetailsFragment)allResult.get(marker).infoWindow.getWindowFragment()).setData(latLng,map,marker.getPosition(),allResult.get(marker).result.name,/*allResult.get(marker).result.*/"");
        return true;
    }

    public void loadMap(){
        String url="https://maps.googleapis.com/maps/api/place/nearbysearch/json?location="+latLng.latitude+","+latLng.longitude+"&radius=10000&type="+type+"&key=AIzaSyCxpy7Lfdz45gEwJNyg5zgEWQxpwm0xeZk";
        System.out.println(url);




        AsyncHttpClient client=new AsyncHttpClient();
        try {
            KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
            trustStore.load(null, null);
            MySSLSocketFactory sf = new MySSLSocketFactory(trustStore);
            sf.setHostnameVerifier(MySSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
            client.setSSLSocketFactory(sf);
        }
        catch (Exception e) {}

        client.get(getApplicationContext(), url,new JsonHttpResponseHandler(){


            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    System.out.println("1. "+response.toString());
                    //JSONArray dataArray=response.getJSONArray("results");

                    Gson gson=new Gson();
                    Nearest nearest=gson.fromJson(response.toString(),Nearest.class);
                    boolean isFirstTime=true;
                    for (Result result:nearest.results
                            ) {
                        final Marker marker = map.addMarker(new MarkerOptions().position(new LatLng(result.geometry.location.lat, result.geometry.location.lng)).snippet(RECYCLER_VIEW).icon(bitmapDescriptor));
                        if(isFirstTime){
                            map.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(result.geometry.location.lat, result.geometry.location.lng)));
                            isFirstTime=false;
                        }
                        map.animateCamera(CameraUpdateFactory.zoomTo(13.0f));
                        final int offsetX = (int) getResources().getDimension(R.dimen.marker_offset_x);
                        final int offsetY = (int) getResources().getDimension(R.dimen.marker_offset_y);

                        final InfoWindow.MarkerSpecification markerSpec =
                                new InfoWindow.MarkerSpecification(offsetX, offsetY);

                        NearDetailsFragment nearDetailsFragment=new NearDetailsFragment();


                        InfoWindow infoWindow = new InfoWindow(marker, markerSpec,nearDetailsFragment);
                        nearDetailsFragment.drawPolyline(map,latLng,new LatLng(result.geometry.location.lat, result.geometry.location.lng));

                        allResult.put(marker,new InfoMap(infoWindow,result));

                    }



                } catch (Exception e) {
                    e.printStackTrace();
                }
            }


            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                try {
                    System.out.println("2. "+response.toString());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                try {
                    System.out.println("3. "+responseString.toString());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                try {
                    System.out.println("4. "+responseString.toString());

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                try {
                    System.out.println("5. "+errorResponse.toString());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                try {
                    System.out.println("6. "+errorResponse.toString());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            this.finish();
            return true;
        }
        return false;
    }


}
class InfoMap{
    InfoWindow infoWindow;
    Result result;

    public InfoMap(InfoWindow infoWindow, Result result) {
        this.infoWindow = infoWindow;
        this.result = result;
    }
}
