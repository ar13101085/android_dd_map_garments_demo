package com.dds.arif.drfm_b.Activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

import com.dds.arif.drfm_b.R;
import com.google.android.gms.maps.OnStreetViewPanoramaReadyCallback;
import com.google.android.gms.maps.StreetViewPanorama;
import com.google.android.gms.maps.SupportStreetViewPanoramaFragment;
import com.google.android.gms.maps.model.LatLng;

import io.realm.Realm;
import io.realm.RealmResults;

public class StreetViewActivity extends AppCompatActivity {

    // George St, Sydney
    private static final LatLng SYDNEY = new LatLng(-33.87365, 151.20689);
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.street_view);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        /*Realm realm = Realm.getDefaultInstance();
        try {
            // ... Do something ...
            Dog dog=new Dog();
            dog.age=12;
            dog.name="Vau";
            realm.beginTransaction();
            realm.copyToRealm(dog);
            realm.commitTransaction();
            RealmResults<Dog> dogs=realm.where(Dog.class).findAll();
            for (Dog d:dogs){
                System.out.println(d);
            }
        } finally {
            realm.close();
        }*/
        final double lat=getIntent().getDoubleExtra("lat",0);
        final double lng=getIntent().getDoubleExtra("lng",0);

        SupportStreetViewPanoramaFragment streetViewPanoramaFragment =
                (SupportStreetViewPanoramaFragment)
                        getSupportFragmentManager().findFragmentById(R.id.streetviewpanorama);
        streetViewPanoramaFragment.getStreetViewPanoramaAsync(
                new OnStreetViewPanoramaReadyCallback() {
                    @Override
                    public void onStreetViewPanoramaReady(StreetViewPanorama panorama) {
                        // Only set the panorama to SYDNEY on startup (when no panoramas have been
                        // loaded which is when the savedInstanceState is null).
                        if (savedInstanceState == null) {
                            //panorama.setPosition(SYDNEY);
                            panorama.setPosition(new LatLng(lat,lng));

                        }
                        panorama.setPosition(new LatLng(lat,lng));
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
