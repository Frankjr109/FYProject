package com.example.projectdemo;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.projectdemo.ui.main.PlaceholderFragment;
import com.google.android.gms.maps.model.LatLng;


import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;


public class AddDonationsListing extends AppCompatActivity implements OnMapReadyCallback, LocationListener {

    private GoogleMap googleMap;
    private MapView mapView;
    private Marker selectedMarker;
    private LocationManager locationManager;
    private EditText titleEditText, descriptionEditText;
    private TextView locationText;
    private Button addDonationsBtn;

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_donations_listing);

        titleEditText = findViewById(R.id.donationsTitle);
        descriptionEditText = findViewById(R.id.donationsDescription);
        locationText = findViewById(R.id.donationsLocation);
        addDonationsBtn = findViewById(R.id.addDonationButton);


        mapView = findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);


        addDonationsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

                if(titleEditText.getText().toString().isEmpty()){
                    Utility.showToast(AddDonationsListing.this, "Please add a title!");
                    return;
                }

                if(descriptionEditText.getText().toString().isEmpty()){
                    Utility.showToast(AddDonationsListing.this, "Please add a description!");
                    return;
                }

                if(locationText.getText().toString().isEmpty()){
                    Utility.showToast(AddDonationsListing.this, "Please tap on the map to select a pickup point location!");
                    return;
                }

                Map<String, Object> donationListing = new HashMap<>();
                donationListing.put("title", titleEditText.getText().toString());
                donationListing.put("description", descriptionEditText.getText().toString());
                donationListing.put("location", locationText.getText().toString());
                donationListing.put("userID", currentUser.getUid());

                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference reference = database.getReference("Users").child(currentUser.getUid());
                reference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        User user1 = snapshot.getValue(User.class);
                        donationListing.put("user", Objects.equals( user1.getFullName(), "") ? "Unknown User":  user1.getFullName());

                        Utility.getDonationsCollection().add(donationListing).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentReference> task) {
                                Utility.showToast(AddDonationsListing.this, "Listing added!");
                                finish();
                            }
                        });

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Utility.showToast(AddDonationsListing.this,"Error adding the listing!");
                    }
                });



            }
        });
    }


    @Override
    public void onMapReady(@NonNull GoogleMap map) {
        googleMap = map;

        // Set initial map position and zoom level
        com.google.android.gms.maps.model.LatLng initialLatLng = new com.google.android.gms.maps.model.LatLng(53.3498, -6.2603); //Dublin coordinates
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(initialLatLng, 10));

        // Enable zoom gestures
        googleMap.getUiSettings().setZoomGesturesEnabled(true);

        // Enable scroll gestures (map dragging)
        googleMap.getUiSettings().setScrollGesturesEnabled(true);

        // Request location permission if not granted
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);
        } else {
            getLocation();
        }

        // Handle marker selection
        googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                if (selectedMarker != null) {
                    selectedMarker.remove(); // Remove previous marker
                }

                selectedMarker = googleMap.addMarker(new MarkerOptions()
                        .position(marker.getPosition())
                        .title("Selected Location"));

                Toast.makeText(getApplicationContext(), marker.getPosition().toString(),Toast.LENGTH_LONG).show();


                return true;
            }
        });

        // Set map click listener
        googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                if (selectedMarker != null) {
                    selectedMarker.remove(); // Remove previous marker
                }

                // Add new marker for clicked location
                selectedMarker = googleMap.addMarker(new MarkerOptions()
                        .position(latLng)
                        .title("Selected Location"));

                Toast.makeText(getApplicationContext(), selectedMarker.getPosition().toString(),Toast.LENGTH_LONG).show();

                locationText.setText(String.format("%s,%s", selectedMarker.getPosition().latitude, selectedMarker.getPosition().longitude));
            }
        });
    }

    private void getLocation() {
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        // Request location updates
        if (locationManager != null) {
            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                Location lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                if (lastKnownLocation != null) {

                    // Update map with current location
                    LatLng currentLatLng = new LatLng(lastKnownLocation.getLatitude(), lastKnownLocation.getLongitude());
                    googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 12));

                    Toast.makeText(this, currentLatLng.toString(), Toast.LENGTH_LONG).show();

                    // Add marker for current location
                    if (selectedMarker != null) {
                        selectedMarker.remove(); // Remove previous marker
                    }
                    selectedMarker = googleMap.addMarker(new MarkerOptions()
                            .position(currentLatLng)
                            .title("Current Location"));

                    locationText.setText(String.format("%s,%s", selectedMarker.getPosition().latitude, selectedMarker.getPosition().longitude));

                }
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getLocation();
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    public void onLocationChanged(@NonNull Location location) {

    }

}