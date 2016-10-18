package com.example.matth.finalapp.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.matth.finalapp.MenuActivity;
import com.example.matth.finalapp.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class SettingsFragment extends Fragment implements View.OnClickListener {


    public SettingsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_settings, container, false);
        ((MenuActivity) getActivity()).getSupportActionBar().setTitle("Settings");
        rootView.findViewById(R.id.logOutButton).setOnClickListener(this);

        return rootView;
    }

    @Override
    public void onClick(View v) {

        if(v.getId() == R.id.logOutButton){

            ((MenuActivity) getActivity()).setLoggedin(false);
            ((MenuActivity) getActivity()).removeToken();
            if (((MenuActivity) getActivity()).getAuthToken() == "") {
                ((MenuActivity) getActivity()).redirectToLogin();
            }
        }
    }
}
