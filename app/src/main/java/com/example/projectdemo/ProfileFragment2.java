package com.example.projectdemo;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


public class ProfileFragment2 extends Fragment {

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){

        View view = inflater.inflate(R.layout.fragment_profile2, container, false);
        return view;
    }
    //return inflater.inflate(R.layout.fragment_profile2)

}