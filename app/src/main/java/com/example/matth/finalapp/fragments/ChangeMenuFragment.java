package com.example.matth.finalapp.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.matth.finalapp.MenuActivity;
import com.example.matth.finalapp.R;


public class ChangeMenuFragment extends Fragment {

    public ChangeMenuFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_change_menu, container, false);
        ((MenuActivity) getActivity()).getSupportActionBar().setTitle("Change Menu");
        return rootView;
    }
}
