package com.example.matth.finalapp.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.matth.finalapp.MenuActivity;
import com.example.matth.finalapp.R;
import com.example.matth.finalapp.WaiterActivity;
import com.google.android.gms.plus.PlusOneButton;

/**
 * A fragment with a Google +1 button.
 */
public class WaitersFragment extends Fragment {

    ListView listView;

    public WaitersFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_waiters, container, false);
        setHasOptionsMenu(true);
        ((MenuActivity) getActivity()).getSupportActionBar().setTitle("Waiters");

        listView = (ListView) rootView.findViewById(R.id.waiter_list);

        String[] values = new String[] { "Jef De Ober",
                "Matthias de beste",
                "beste ober ter wereld",
                "i'm the one you need",
                "slechtste ober",
        };
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, values);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int itemPosition = position;
                String itemValue = (String) listView.getItemAtPosition(position);

                ((MenuActivity) getActivity()).changeToWaiterDetailFragment(itemValue);
                //Intent waiterIntent = new Intent(getContext(), WaiterActivity.class);
                //waiterIntent.putExtra("itemName", itemValue);
                //startActivity(waiterIntent);
            }
        });

        return rootView;
    }

    /*@Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        MenuItem menuitem = (MenuItem) menu.findItem(R.id.shopping_cart);
        menuitem.setVisible(false);
    }*/
}
