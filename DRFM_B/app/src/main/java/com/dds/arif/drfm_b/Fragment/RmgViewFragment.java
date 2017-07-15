package com.dds.arif.drfm_b.Fragment;


import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.dds.arif.drfm_b.Activity.NearestLocationActivity;
import com.dds.arif.drfm_b.Activity.StreetViewActivity;
import com.dds.arif.drfm_b.R;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.StreetViewPanoramaCamera;

/**
 * A simple {@link Fragment} subclass.
 */
public class RmgViewFragment extends Fragment {


    public RmgViewFragment() {
        // Required empty public constructor
    }

    LatLng latLng;
    TextView textViewName;
    TextView textViewDetails;
    Button nearHospital;
    Button nearFireStation;
    Button buttonTakeSurvey;
    Button buttonShowStreetView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView=inflater.inflate(R.layout.garments_map_details,container,false);
        textViewName= (TextView) rootView.findViewById(R.id.textViewName);
        textViewDetails= (TextView) rootView.findViewById(R.id.textViewDetails);


        nearHospital= (Button) rootView.findViewById(R.id.nearHospital);
        nearFireStation= (Button) rootView.findViewById(R.id.nearFireStation);
        buttonTakeSurvey= (Button) rootView.findViewById(R.id.buttonTakeSurvey);
        buttonShowStreetView= (Button) rootView.findViewById(R.id.buttonShowStreetView);


        buttonShowStreetView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getContext(),StreetViewActivity.class);
                intent.putExtra("lat",latLng.latitude);
                intent.putExtra("lng",latLng.longitude);
                startActivity(intent);
            }
        });

        buttonTakeSurvey.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(getContext());
                dialog.setContentView(R.layout.take_survey);
                Button button= (Button) dialog.findViewById(R.id.btn_done);
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(getContext(), "Thank you for your response.", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }
                });
                dialog.show();
            }
        });

        nearHospital.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getContext(),NearestLocationActivity.class);
                intent.putExtra("lat",latLng.latitude);
                intent.putExtra("lng",latLng.longitude);


                intent.putExtra("type","hospital");
                startActivity(intent);
            }
        });

        nearFireStation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getContext(),NearestLocationActivity.class);
                intent.putExtra("lat",latLng.latitude);
                intent.putExtra("lng",latLng.longitude);


                intent.putExtra("type","fire_station");
                startActivity(intent);
            }
        });


        /*View rootView=inflater.inflate(R.layout.markar_details,container,false);
        details= (TextView) rootView.findViewById(R.id.text_view);
        nearestHospital= (Button) rootView.findViewById(R.id.nearHospital);
        nearestFirestation= (Button) rootView.findViewById(R.id.nearFirestation);


        nearestHospital.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getContext(),NearestLocationActivity.class);
                intent.putExtra("lat",latLng.latitude);
                intent.putExtra("lng",latLng.longitude);


                intent.putExtra("type","hospital");
                startActivity(intent);
            }
        });

        nearestFirestation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getContext(),NearestLocationActivity.class);
                intent.putExtra("lat",latLng.latitude);
                intent.putExtra("lng",latLng.longitude);


                intent.putExtra("type","fire_station");
                startActivity(intent);
            }
        });*/

        return rootView;
    }

    public void setData(LatLng latLng,String name,String details){
        //details.setText(information);
        this.latLng=latLng;
        textViewName.setText(name);
        textViewDetails.setText(details);
    }

}
