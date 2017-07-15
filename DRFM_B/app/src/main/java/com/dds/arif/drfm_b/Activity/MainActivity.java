package com.dds.arif.drfm_b.Activity;

import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.appolica.interactiveinfowindow.InfoWindow;
import com.appolica.interactiveinfowindow.InfoWindowManager;
import com.appolica.interactiveinfowindow.fragment.MapInfoWindowFragment;
import com.dds.arif.drfm_b.Fragment.RmgViewFragment;
import com.dds.arif.drfm_b.Model.GarmentsApi.Garments;
import com.dds.arif.drfm_b.Model.MapDraw;
import com.dds.arif.drfm_b.R;
import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.MySSLSocketFactory;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.security.KeyStore;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cz.msebera.android.httpclient.Header;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,GoogleMap.OnMarkerClickListener {


    private static final String RECYCLER_VIEW = "RECYCLER_VIEW_MARKER";
    private static final String FORM_VIEW = "FORM_VIEW_MARKER";
    RmgViewFragment rmgViewFragment;

    public GoogleMap mMap;
    private InfoWindow recyclerWindow;
    private InfoWindow formWindow;
    private InfoWindowManager infoWindowManager;

    Map<Marker,GarmentsInfo> markerGarmentsInfoMap=new HashMap<Marker,GarmentsInfo>();
    ArrayList<GarmentsInfo> garmentsInfos=new ArrayList<>();

    ArrayList<MapDraw> mapDraws=new ArrayList<>();
    boolean isMapDrawEnable=false;

    LinearLayout map_draw_controller;
    Button done_btn;
    Button btn_cancel;

    FloatingActionMenu menu;
    FloatingActionButton addGarments;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        map_draw_controller= (LinearLayout) findViewById(R.id.map_draw_controller);
        done_btn= (Button) findViewById(R.id.done_btn);
        btn_cancel= (Button) findViewById(R.id.btn_cancel);
        map_draw_controller.setVisibility(View.GONE);

        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ImageView notification_btn= (ImageView) toolbar.findViewById(R.id.notification_btn);

        notification_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this,NotificationActivity.class);
                startActivity(intent);
            }
        });


        final android.support.design.widget.FloatingActionButton btn= (android.support.design.widget.FloatingActionButton) findViewById(R.id.fab);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*Animation show_fab_1 = AnimationUtils.loadAnimation(MainActivity.this, R.anim.fab_ani);
                show_fab_1.setDuration(200L);
                toolbar.startAnimation(show_fab_1);*/
                isMapDrawEnable=true;
                map_draw_controller.setVisibility(View.VISIBLE);
                Toast.makeText(MainActivity.this, "Please long press on map to draw a garments.", Toast.LENGTH_SHORT).show();

            }
        });
        done_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isMapDrawEnable=false;
                map_draw_controller.setVisibility(View.GONE);
                if(mapDraws.size()>=2){

                    Polyline line = mMap.addPolyline(
                            new PolylineOptions().add(mapDraws.get(0).latLng,
                                    mapDraws.get(mapDraws.size()-1).latLng
                            ).width(10).color(Color.BLUE).geodesic(true)
                    );
                }
                for (MapDraw mapDraw:mapDraws
                        ) {
                    mapDraw.marker.remove();
                }
                if(mapDraws.size()<2){
                    mapDraws.clear();
                    return;
                }

                plotGarments(mMap,new GarmentsInfo("Test Garments","Brands : Primark\nWorkers : 15000\nexport market : Rusia",mapDraws.get(0).latLng));

            }
        });

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isMapDrawEnable=false;
                map_draw_controller.setVisibility(View.GONE);
                for (MapDraw mapDraw:mapDraws
                        ) {
                    mapDraw.marker.remove();
                }
                mapDraws.clear();
            }
        });
       /* map_draw_controller= (LinearLayout) findViewById(R.id.map_draw_controller);
        done_btn= (Button) findViewById(R.id.done_btn);
        btn_cancel= (Button) findViewById(R.id.btn_cancel);
        map_draw_controller.setVisibility(View.GONE);

        done_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isMapDrawEnable=false;
                map_draw_controller.setVisibility(View.GONE);
            }
        });

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isMapDrawEnable=false;
                map_draw_controller.setVisibility(View.GONE);
            }
        });


        menu= (FloatingActionMenu) findViewById(R.id.menu);
        addGarments= (FloatingActionButton) findViewById(R.id.add_garments);

        addGarments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isMapDrawEnable=true;
                map_draw_controller.setVisibility(View.VISIBLE);
            }
        });*/

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);



        //<editor-fold desc="Map Configuration">
        garmentsInfos.add(new GarmentsInfo("আলিফ গার্মেন্টস লিঃ","Brands : Primark\nWorkers : 15000\nexport market : Rusia",new LatLng(23.7688018,90.334449)));
        garmentsInfos.add(new GarmentsInfo("ইয়েলো","Brands : Primark\nWorkers : 15000\nexport market : Rusia",new LatLng(23.8186261,90.3309285)));
        garmentsInfos.add(new GarmentsInfo("পাইওনিয়ার অ্যাপারেলস লিমিটেড","Brands : Primark\nWorkers : 15000\nexport market : Rusia",new LatLng(23.837627, 90.364402)));
        garmentsInfos.add(new GarmentsInfo("কনকর্ড গার্মেন্টস লিমিটেড","Brands : Primark\nWorkers : 15000\nexport market : Rusia",new LatLng(23.820354, 90.363716)));
        garmentsInfos.add(new GarmentsInfo("Bangladesh Garment Manufacturers and Exporters Association","Brands : Primark\nWorkers : 15000\nexport market : Rusia",new LatLng(23.757145, 90.399421)));

        final MapInfoWindowFragment mapInfoWindowFragment =
                (MapInfoWindowFragment) getSupportFragmentManager().findFragmentById(R.id.infoWindowMap);

        infoWindowManager = mapInfoWindowFragment.infoWindowManager();
        //infoWindowManager.setHideOnFling(true);



        mapInfoWindowFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {

                mMap=googleMap;
                /*for (GarmentsInfo garmentsInfo:garmentsInfos
                        ) {
                    plotGarments(googleMap, garmentsInfo);
                }


                googleMap.animateCamera(CameraUpdateFactory.zoomTo(10.0f));*/
                loadAllGarments();
                googleMap.setOnMarkerClickListener(MainActivity.this);

                /*googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                    @Override
                    public void onMapClick(LatLng latLng) {
                        System.out.println("Click Working........");
                        if(isMapDrawEnable){
                            mapDraws.add(new MapDraw(mapDraws.size(),latLng));

                            final Marker marker = mMap.addMarker(new MarkerOptions().position(latLng).snippet(RECYCLER_VIEW));
                            mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                            if(mapDraws.size()<2)
                                return;

                            System.out.println("Working........");
                            Polyline line = mMap.addPolyline(
                                    new PolylineOptions().add(mapDraws.get(mapDraws.size()-2).latLng,
                                            mapDraws.get(mapDraws.size()-1).latLng
                                    ).width(10).color(Color.BLUE).geodesic(true)
                            );
                        }else{
                            mapDraws.clear();
                        }
                    }
                });*/
                googleMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
                    @Override
                    public void onMapLongClick(LatLng latLng) {

                        if(isMapDrawEnable){


                            final Marker marker = mMap.addMarker(new MarkerOptions().position(latLng).snippet(RECYCLER_VIEW));
                            mapDraws.add(new MapDraw(mapDraws.size(),latLng,marker));

                            if(mapDraws.size()<2)
                                return;

                            System.out.println("Working........");
                            Polyline line = mMap.addPolyline(
                                    new PolylineOptions().add(mapDraws.get(mapDraws.size()-2).latLng,
                                            mapDraws.get(mapDraws.size()-1).latLng
                                    ).width(10).color(Color.BLUE).geodesic(true)
                            );
                        }else{
                            mapDraws.clear();
                        }
                    }
                });

                googleMap.setOnMyLocationChangeListener(new GoogleMap.OnMyLocationChangeListener() {
                    @Override
                    public void onMyLocationChange(Location location) {
                        mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(location.getLatitude(),location.getLongitude())));
                    }
                });

            }
        });
        //</editor-fold>












    }

    public void loadAllGarments(){
        String url = "http://www.rmg.project.dreamdoorsoft.com/api/garments";

        AsyncHttpClient client=new AsyncHttpClient();
        /*try {
            KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
            trustStore.load(null, null);
            MySSLSocketFactory sf = new MySSLSocketFactory(trustStore);
            sf.setHostnameVerifier(MySSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
            client.setSSLSocketFactory(sf);
        }
        catch (Exception e) {}*/
        //RequestParams requestParams=new RequestParams();

        client.get(getApplicationContext(), url,new JsonHttpResponseHandler(){



            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                try {
                    Gson gson=new Gson();
                    Type listType = new TypeToken<List<Garments>>()
                    {
                    }.getType();
                    List<Garments> garmentsList=gson.fromJson(response.toString(),listType);

                    for (Garments garments:garmentsList
                            ) {
                        try {
                            String s="Product Type : "+garments.factory.data.product_type+"\n"+
                                    "Export Destination : "+garments.factory.data.export_destination+"\n"+
                                    "Brand : "+garments.factory.data.brand+"\n"+
                                    "Works : "+garments.factory.data.workers+"\n";
                            garmentsInfos.add(new GarmentsInfo(garments.factory.data.garment_name,s,new LatLng(Double.parseDouble(garments.factory.data.lat),Double.parseDouble(garments.factory.data.lng))));

                        } catch (NumberFormatException e) {
                            e.printStackTrace();
                        }

                    }
                    for (GarmentsInfo garmentsInfo:garmentsInfos
                            ) {
                        plotGarments(mMap, garmentsInfo);
                        mMap.moveCamera(CameraUpdateFactory.newLatLng(garmentsInfo.latLng));
                    }


                    mMap.animateCamera(CameraUpdateFactory.zoomTo(10.0f));




                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }


    private void plotGarments(GoogleMap googleMap, GarmentsInfo garmentsInfo) {
        final Marker marker1 = googleMap.addMarker(new MarkerOptions().position(garmentsInfo.latLng).snippet(RECYCLER_VIEW).icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_garments_markar)));

        final int offsetX = (int) getResources().getDimension(R.dimen.marker_offset_x);
        final int offsetY = (int) getResources().getDimension(R.dimen.marker_offset_y);

        final InfoWindow.MarkerSpecification markerSpec =
                new InfoWindow.MarkerSpecification(offsetX, offsetY);


        //recyclerWindow = new InfoWindow(marker1, markerSpec,new RmgViewFragment());
        garmentsInfo.infoWindow=new InfoWindow(marker1, markerSpec,new RmgViewFragment());
        markerGarmentsInfoMap.put(marker1,garmentsInfo);
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(garmentsInfo.latLng));
    }

    @Override
    public boolean onMarkerClick(Marker marker) {

        if(markerGarmentsInfoMap.get(marker)==null)
            return false;
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

        ((RmgViewFragment)infoWindow.getWindowFragment()).setData(marker.getPosition(),markerGarmentsInfoMap.get(marker).name,markerGarmentsInfoMap.get(marker).details);

        return true;
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        if(id==R.id.nav_fire_station){
            Intent intent=new Intent(MainActivity.this,NearestLocationActivity.class);
            intent.putExtra("lat",23.993739);
            intent.putExtra("lng",90.405247);
            intent.putExtra("type","fire_station");
            startActivity(intent);
        }else if(id==R.id.nav_garments){

        }else if(id==R.id.nav_hospital){
            Intent intent=new Intent(MainActivity.this,NearestLocationActivity.class);
            intent.putExtra("lat",23.993739);
            intent.putExtra("lng",90.405247);
            intent.putExtra("type","hospital");
            startActivity(intent);
        }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


}
