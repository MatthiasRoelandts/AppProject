package com.example.matth.finalapp.fragments;


import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.matth.finalapp.MenuActivity;
import com.example.matth.finalapp.R;
import com.example.matth.finalapp.objects.Business;
import com.example.matth.finalapp.objects.Itemcategory;
import com.example.matth.finalapp.objects.Owner;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class RestaurantFragment extends Fragment implements View.OnClickListener {

    public static String DATA_RECEIVE = "";
    private FragmentTransaction fragmentTransaction;
    private String args;

    ListView listView;
    List<Business> restaurantList;
    List<String> restaurantStringList;

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

        listView = (ListView) rootView.findViewById(R.id.restaurant_list);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int itemPosition = position;
                Business itemValue = (Business) listView.getItemAtPosition(position);
                ((MenuActivity) getActivity()).changeToBusinessDetailFragment(itemValue);
            }
        });
        restaurantList = new ArrayList<>();
        restaurantStringList = new ArrayList<>();
        getRestaurants();
        return rootView;

    }

    private void fillRestaurantList() {
        for(Business restaurant: restaurantList) {
            restaurantStringList.add(restaurant.getName());
        }
        ArrayAdapter<Business> adapter = new ArrayAdapter<Business>(getContext(), android.R.layout.simple_list_item_1, restaurantList);
        listView.setAdapter(adapter);
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
                ((MenuActivity) getActivity()).turnMenuOff();
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

    private void getRestaurants() {
        new AsyncTask<Void, Object, List<Business>>() {
            @Override
            protected List<Business> doInBackground(Void... params) {
                final String url = "http://10.0.2.2:8080/getbusinesses";
                HttpStatus status = null;
                RestTemplate restTemplate = new RestTemplate();
                restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
                restTemplate.getMessageConverters().add(new StringHttpMessageConverter());
                ResponseEntity response = null;
                List<Business> list = null;
                HttpHeaders header = new HttpHeaders();
                header.add("Authorization", ((MenuActivity) getActivity()).getAuthToken());
                try {
                    HttpEntity<Owner> request = new HttpEntity(header);
                    response = restTemplate.exchange(url, HttpMethod.GET, request, Business[].class);
                    status = response.getStatusCode();
                    list = Arrays.asList((Business[]) response.getBody());
                } catch (HttpClientErrorException e) {
                    if (e.getStatusCode() == HttpStatus.BAD_REQUEST) {

                    }
                }
                return list;
            }

            @Override
            protected void onPostExecute(List<Business> list) {
                restaurantList = list;
                fillRestaurantList();
            }
        }.execute();
    }
}
