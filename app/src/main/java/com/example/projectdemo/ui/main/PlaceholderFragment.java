package com.example.projectdemo.ui.main;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.projectdemo.Adapter.DonationsListAdapter;
import com.example.projectdemo.DonationListing;
import com.example.projectdemo.DonationListingActivity;
import com.example.projectdemo.MessageAdapter;
import com.example.projectdemo.R;
import com.example.projectdemo.Utility;
import com.example.projectdemo.databinding.FragmentMyDonationsBinding;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

/**
 * A placeholder fragment containing a simple view.
 */
public class PlaceholderFragment extends Fragment {

    private static final String ARG_SECTION_NUMBER = "section_number";

    private PageViewModel pageViewModel;
    private FragmentMyDonationsBinding binding;
    private ArrayList<DonationListing> listings;
    RecyclerView listingsRecyclerView;

    public static PlaceholderFragment newInstance(int index) {
        PlaceholderFragment fragment = new PlaceholderFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(ARG_SECTION_NUMBER, index);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pageViewModel = new ViewModelProvider(this).get(PageViewModel.class);
        int index = 1;
        if (getArguments() != null) {
            index = getArguments().getInt(ARG_SECTION_NUMBER);
        }
        pageViewModel.setIndex(index);
        listings = new ArrayList<>();
    }

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {

        binding = FragmentMyDonationsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        int index = getArguments().getInt(ARG_SECTION_NUMBER);

        // Inflate different layouts based on the index value
        switch (index) {
            case 1:
                // First tab layout
                root = inflater.inflate(R.layout.activity_main_donations, container, false);
                break;
            case 2:
                // Second tab layout
                root = inflater.inflate(R.layout.donations_map_view, container, false);

                break;
            // Add more cases for additional tabs if needed
            default:
                break;
        }


        if(index == 1){
            listingsRecyclerView = root.findViewById(R.id.recycler_view_donations);
            getDonationsFromFirestore();



        }

        if(index == 2){
            MapView mapView = root.findViewById(R.id.listingMapView);
            mapView.onCreate(savedInstanceState);
            mapView.getMapAsync(new OnMapReadyCallback() {
                @Override
                public void onMapReady(@NonNull GoogleMap map) {

                    GoogleMap googleMap = map;
                    mapView.onResume();
                    // Set initial map position and zoom level
                    LatLng initialLatLng = new LatLng(53.3498, -6.2603); //Dublin coordinates
                    googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(initialLatLng, 10));

                    // Enable zoom gestures
                    googleMap.getUiSettings().setZoomGesturesEnabled(true);

                    // Enable scroll gestures (map dragging)
                    googleMap.getUiSettings().setScrollGesturesEnabled(true);

                    getDonationsFromFirestore(googleMap);
                }
            });

        }


        return root;
    }

    void getDonationsFromFirestore(){
        CollectionReference donationsRef =  FirebaseFirestore.getInstance().collection("donations");

        donationsRef.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for(DocumentSnapshot snapshot: queryDocumentSnapshots){
                    String title = snapshot.getData().get("title").toString();
                    String desc = snapshot.getData().get("description").toString();
                    String location = snapshot.getData().get("location").toString();
                    String user = snapshot.getData().get("user").toString();
                    String userID = snapshot.getData().get("userID").toString();

                    DonationListing listing = new DonationListing(title,desc, location, user, userID);
                    listings.add(listing);
                }


                //set up recycler view
                DonationsListAdapter adapter = new DonationsListAdapter(getActivity(), listings);
                listingsRecyclerView.setAdapter(adapter);
                LinearLayoutManager llm = new LinearLayoutManager(getActivity());
                llm.setStackFromEnd(true);
                listingsRecyclerView.setLayoutManager(llm);

            }
        });
    }

    // method overloading
    void getDonationsFromFirestore(GoogleMap map){
        CollectionReference donationsRef =  FirebaseFirestore.getInstance().collection("donations");

        donationsRef.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for(DocumentSnapshot snapshot: queryDocumentSnapshots){
                    String title = snapshot.getData().get("title").toString();
                    String desc = snapshot.getData().get("description").toString();
                    String location = snapshot.getData().get("location").toString();
                    String user = snapshot.getData().get("user").toString();
                    String userID = snapshot.getData().get("userID").toString();

                    DonationListing listing = new DonationListing(title,desc, location, user, userID);
                    listings.add(listing);
                }


                //set up map markers
               // "2432,234" = ["2432","234"]
                for(DonationListing listing: listings){
                    LatLng listingLoc = new LatLng(
                            Float.parseFloat(listing.getLocation().split(",")[0]),
                            Float.parseFloat(listing.getLocation().split(",")[1]));

                    map.addMarker(new MarkerOptions()
                            .position(listingLoc)
                            .title(listing.getTitle())
                            .snippet(listing.getDescription()));

                }

               map.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                   @Override
                   public boolean onMarkerClick(@NonNull Marker marker) {
                       for(DonationListing listing: listings){
                           if(listing.getTitle().equals(marker.getTitle())){
                               Intent i = new Intent(getActivity(), DonationListingActivity.class);
                               startActivity(i);
                               break;
                           }
                       }

                       return false;
                   }
               });

            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}