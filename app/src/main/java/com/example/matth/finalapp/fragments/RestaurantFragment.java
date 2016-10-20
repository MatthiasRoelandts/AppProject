package com.example.matth.finalapp.fragments;


import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.matth.finalapp.MenuActivity;
import com.example.matth.finalapp.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class RestaurantFragment extends Fragment implements View.OnClickListener {


    public static String DATA_RECEIVE = "";
    private FragmentTransaction fragmentTransaction;
    private String args;


    public RestaurantFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_restaurant, container, false);
        ((MenuActivity) getActivity()).getSupportActionBar().setTitle("Restaurants");
        if (getArguments() != null) {
            args = getArguments().getString("parentpage");
        }

        return rootView;

    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        view.findViewById(R.id.floating_add_business_button).setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.floating_add_business_button:
                AddRestaurantFragment addRestaurantFragment = new AddRestaurantFragment();
                getFragmentManager().beginTransaction()
                        .replace(((ViewGroup) getView().getParent()).getId(), addRestaurantFragment)
                        .addToBackStack(null)
                        .commit();
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        Bundle args = getArguments();
        if (args != null && args.getString(DATA_RECEIVE) == "showList") {
            //showReceivedData.setText(args.getString(DATA_RECEIVE));

        }
    }

    @Override
    public void onResume() {
        super.onResume();

        if (args == "login") {
            //((MenuActivity) getActivity()).getSupportActionBar().hide();
            ((MenuActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(false);
            ((MenuActivity) getActivity()).lockDrawer();
        }

    }

    @Override
    public void onStop() {
        super.onStop();
        if (args == "login") {
            //((MenuActivity) getActivity()).getSupportActionBar().show();
            ((MenuActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            ((MenuActivity) getActivity()).unlockDrawer();
        }
    }

    private class GetRestaurants extends AsyncTask<Object, Void, Object> {


        @Override
        protected Object doInBackground(Object... params) {
            return null;
        }
    }
}
