package com.dds.arif.drfm_b.Fragment;


import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.dds.arif.drfm_b.Model.Map_Direction.Direction;
import com.dds.arif.drfm_b.R;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.gson.Gson;
import com.google.maps.android.PolyUtil;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.MySSLSocketFactory;

import org.json.JSONArray;
import org.json.JSONObject;

import java.security.KeyStore;
import java.util.List;

import cz.msebera.android.httpclient.Header;


/**
 * A simple {@link Fragment} subclass.
 */
public class NearDetailsFragment extends Fragment {


    public NearDetailsFragment() {
        // Required empty public constructor
    }

    TextView textViewName;
    TextView textViewDetails;
    Button drawRoute;
    LatLng latLngGarments;

    LatLng selectedLocation;
    GoogleMap map;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view=inflater.inflate(R.layout.near_by_details, container, false);

        textViewName= (TextView) view.findViewById(R.id.textViewName);
        textViewDetails= (TextView) view.findViewById(R.id.textViewDetails);



        return view;
    }

    public void setData(LatLng latLngGarments,GoogleMap map,LatLng selectedLocation,String name,String details){
        textViewName.setText(name);
        textViewDetails.setText(details);
        this.selectedLocation=selectedLocation;
        this.map=map;
        this.latLngGarments=latLngGarments;

        getDetails(latLngGarments,selectedLocation);
    }

    public void getDetails(LatLng source,LatLng destination){
        String url="https://maps.googleapis.com/maps/api/directions/json?origin="+source.latitude+","+source.longitude+"&destination="+destination.latitude+","+destination.longitude+"&mode=driving&key=AIzaSyDar7_4913_-z4GR6j_Ke24QUhiAsAwOTE";
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

        client.get(getContext(), url,new JsonHttpResponseHandler(){


            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    System.out.println("1. "+response.toString());
                    Gson gson=new Gson();
                    Direction direction=gson.fromJson(response.toString(),Direction.class);
                    String s="Distance : "+direction.routes.get(0).legs.get(0).distance.text+"\n";
                    s=s+"Duration : "+direction.routes.get(0).legs.get(0).duration.text+"\n";
                    textViewDetails.setText(s);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void drawPolyline(final GoogleMap gMap, LatLng source, LatLng destination){
        System.out.println("src "+source);
        System.out.println("des "+destination);
        String url="https://maps.googleapis.com/maps/api/directions/json?origin="+source.latitude+","+source.longitude+"&destination="+destination.latitude+","+destination.longitude+"&key=AIzaSyDar7_4913_-z4GR6j_Ke24QUhiAsAwOTE";
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

        client.get(getContext(), url,new JsonHttpResponseHandler(){


            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    System.out.println("1. "+response.toString());
                    String data=response.getJSONArray("routes").getJSONObject(0).getJSONObject("overview_polyline").getString("points");
                    //JSONObject data=response.getJSONArray("routes").getJSONObject(0).getJSONObject("overview_polyline");
                    System.out.println("Final Data "+data);
                    List<LatLng> points = PolyUtil.decode(data);
                    for (int i = 0; i < points.size() - 1; i++) {
                        LatLng src = points.get(i);
                        LatLng dest = points.get(i + 1);

                        // mMap is the Map Object
                        Polyline line = gMap.addPolyline(
                                new PolylineOptions().add(
                                        new LatLng(src.latitude, src.longitude),
                                        new LatLng(dest.latitude,dest.longitude)
                                ).width(10).color(Color.BLUE).geodesic(true)
                        );
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


}
