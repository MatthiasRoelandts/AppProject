package com.example.matth.finalapp.fragments.personnel;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.example.matth.finalapp.MenuActivity;
import com.example.matth.finalapp.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class WaiterDetailFragment extends Fragment {


    public WaiterDetailFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_waiter_detail, container, false);
        String name = getArguments().getString("name");
        ((MenuActivity) getActivity()).getSupportActionBar().setTitle(name);
        return rootView;
    }
}
