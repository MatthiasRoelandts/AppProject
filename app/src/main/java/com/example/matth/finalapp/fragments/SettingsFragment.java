package com.example.matth.finalapp.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.matth.finalapp.MenuActivity;
import com.example.matth.finalapp.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class SettingsFragment extends Fragment {


    public SettingsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_settings, container, false);
        ((MenuActivity) getActivity()).getSupportActionBar().setTitle("Settings");

        Button logOutButton = (Button) rootView.findViewById(R.id.logOutButton);
        logOutButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {

                TokenManager tokenManager = new TokenManager(PreferenceManager.getDefaultSharedPreferences(getActivity()));
                ((MenuActivity) getActivity()).setLoggedin(false);
                tokenManager.removeToken();
                if(((MenuActivity) getActivity()).getAuthToken()==""){
                    Intent intent = new Intent(getActivity(),LoginActivity.class  );
                    startActivity(intent);
                }
            }
        });
        return rootView;
    }

}
