package com.example.projectdemo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;

import okhttp3.internal.Util;

public class DonationListingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donation_listing);

        String title = getIntent().getStringExtra("title");
        String id = getIntent().getStringExtra("id");
        String description = getIntent().getStringExtra("description");
        String location = getIntent().getStringExtra("location");

        TextView titleView = findViewById(R.id.donationTitle);
        TextView descriptionView = findViewById(R.id.donationDescription);
        MapView mapView = findViewById(R.id.donationsPickupMap);
        Button collectButton = findViewById(R.id.collectButton);

        titleView.setText(title);
        descriptionView.setText(description);

        collectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                double lat =  Float.parseFloat(location.split(",")[0]);
                double longitude = Float.parseFloat(location.split(",")[1]);

                String uri = "google.navigation:q="+lat+","+longitude;
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                intent.setPackage("com.google.android.apps.maps");

                if(intent.resolveActivity(getPackageManager()) != null){
                    startActivity(intent);
                }else{
                    Utility.showToast(DonationListingActivity.this, "Maps app not found");
                }

                Utility.getDonationsCollection().document(id).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        finish();
                    }
                });


            }
        });

        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(@NonNull GoogleMap map) {

                GoogleMap googleMap = map;
                mapView.onResume();
                // Set initial map position and zoom level
                LatLng listingLoc = new LatLng(
                        Float.parseFloat(location.split(",")[0]),
                        Float.parseFloat(location.split(",")[1]));
                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(listingLoc, 10));

                // Enable zoom gestures
                googleMap.getUiSettings().setZoomGesturesEnabled(true);

                // Enable scroll gestures (map dragging)
                googleMap.getUiSettings().setScrollGesturesEnabled(true);

                map.addMarker(new MarkerOptions()
                        .position(listingLoc)
                        .title(title));
            }
        });


    }
}