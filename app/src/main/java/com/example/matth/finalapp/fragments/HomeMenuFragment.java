package com.example.matth.finalapp.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.example.matth.finalapp.MenuActivity;
import com.example.matth.finalapp.R;

public class HomeMenuFragment extends Fragment {

    DrawerLayout drawerLayout;

    public HomeMenuFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_home_menu, container, false);
        setHasOptionsMenu(true);

        drawerLayout = (DrawerLayout) rootView.findViewById(R.id.drawerLayout2);

        return rootView;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.shopping_cart) {
            if(drawerLayout.isDrawerOpen(Gravity.RIGHT) == true) {
                drawerLayout.closeDrawer(Gravity.RIGHT);
            }
            else {
                drawerLayout.openDrawer(Gravity.RIGHT);
                ((MenuActivity)getActivity()).closeLeftDrawer();
            }
        }
        return super.onOptionsItemSelected(item);
    }

    public void closeRightDrawer() {
        drawerLayout.closeDrawer(Gravity.RIGHT);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.action_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }
}
