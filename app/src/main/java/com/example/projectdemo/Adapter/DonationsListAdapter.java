package com.example.projectdemo.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.projectdemo.DonationListing;
import com.example.projectdemo.MessageActivity;
import com.example.projectdemo.Model.Chat;
import com.example.projectdemo.R;
import com.example.projectdemo.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

public class DonationsListAdapter extends RecyclerView.Adapter<DonationsListAdapter.ViewHolder> {

    private Context mContext;
    private List<DonationListing> donationListings;

    FirebaseUser fuser;

    public DonationsListAdapter(Context mContext, List<DonationListing> listings){
        this.mContext = mContext;
        this.donationListings = listings;
    }

    @NonNull
    @Override
    public DonationsListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.donation_listing_item, parent, false);
            return new DonationsListAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        DonationListing listing = donationListings.get(position);

        holder.listingTitle.setText(listing.getTitle());
        holder.listingDescription.setText(listing.getDescription());

    }

    @Override
    public int getItemCount() {
        return donationListings.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public TextView listingTitle, listingDescription;;

        public ViewHolder(View itemView){
            super(itemView);

            listingTitle = itemView.findViewById(R.id.listing_title);
            listingDescription = itemView.findViewById(R.id.listing_description);
        }
    }
}
