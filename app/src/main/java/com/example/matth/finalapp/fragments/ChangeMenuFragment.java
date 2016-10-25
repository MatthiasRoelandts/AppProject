package com.example.matth.finalapp.fragments;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.Toast;

import com.example.matth.finalapp.CustomExpandableMenuAdapter;
import com.example.matth.finalapp.MenuActivity;
import com.example.matth.finalapp.R;
import com.example.matth.finalapp.objects.Itemcategory;

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
import org.springframework.web.util.UriComponentsBuilder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class ChangeMenuFragment extends Fragment {

    ExpandableListView expandableListView;
    ExpandableListAdapter expandableListAdapter;
    List<String> expandableListTitle;
    List<Itemcategory> itemcategoryList;
    List<MenuItem> menuItemList;
    HashMap<String, List<String>> expandableListDetail;
    HashMap<Itemcategory, List<com.example.matth.finalapp.objects.MenuItem>> menuMap;

    public ChangeMenuFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_change_menu, container, false);
        ((MenuActivity) getActivity()).getSupportActionBar().setTitle("Change Menu");

        FloatingActionButton fab = (FloatingActionButton) rootView.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MenuActivity) getActivity()).changeToAddItemFragment();
            }
        });

        expandableListView = (ExpandableListView) rootView.findViewById(R.id.expandableMenu);
        getMenu();
        return rootView;
    }

    private void fillList() {
        expandableListDetail = new HashMap<>();
        expandableListTitle = new ArrayList<>();
        for (Itemcategory key : menuMap.keySet()) {
            List<String> values = new ArrayList<>();
            for(com.example.matth.finalapp.objects.MenuItem item: menuMap.get(key)){
                values.add(item.getTitle());
            }
            expandableListTitle.add(key.getName());
            expandableListDetail.put(key.getName(), values);
        }

        expandableListAdapter = new CustomExpandableMenuAdapter(getActivity(), expandableListTitle, expandableListDetail);
        expandableListView.setAdapter(expandableListAdapter);
        expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v,
                                        int groupPosition, int childPosition, long id) {
                Toast.makeText(
                        getContext(),
                        expandableListTitle.get(groupPosition)
                                + " -> "
                                + expandableListDetail.get(
                                expandableListTitle.get(groupPosition)).get(
                                childPosition), Toast.LENGTH_SHORT
                ).show();
                return false;
            }
        });
    }

    private void getMenu() {
        menuMap = new HashMap<>();
        itemcategoryList = new ArrayList<>();
        new AsyncTask<Void, Void, ArrayList<Object>>() {
            ArrayList<Object> responsArray = new ArrayList<Object>();
            @Override
            protected ArrayList<Object> doInBackground(Void... params) {
                final String urlCategory = "http://10.0.2.2:8080/ItemCategory/all/{id}";
                final String urlItem = "http://10.0.2.2:8080/MenuItem/category/{id}";
                HttpStatus status = null;
                RestTemplate restTemplate = new RestTemplate();
                // Add the Jackson and String message converters
                restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
                restTemplate.getMessageConverters().add(new StringHttpMessageConverter());
                ResponseEntity<Itemcategory[]> responseCategory = null;
                ResponseEntity<com.example.matth.finalapp.objects.MenuItem[]> responseItem = null;
                try {
                    final HttpHeaders headers = new HttpHeaders();
                    headers.add("Authorization", ((MenuActivity) getActivity()).getAuthToken());
                    HttpEntity requestCategory = new HttpEntity(headers);
                    Map<String, Integer> categoryParams = new HashMap<String, Integer>();
                    categoryParams.put("id", ((MenuActivity) getActivity()).getBusinessId());
                    UriComponentsBuilder builderCategory = UriComponentsBuilder.fromHttpUrl(urlCategory);
                    responseCategory = restTemplate.exchange(builderCategory.buildAndExpand(categoryParams).toUri(), HttpMethod.GET, requestCategory, Itemcategory[].class);
                    status = responseCategory.getStatusCode();
                    for(Itemcategory category: responseCategory.getBody()) {
                        Map<String, Integer> uriParams = new HashMap<String, Integer>();
                        uriParams.put("id", category.getId());
                        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(urlItem);
                        HttpEntity request = new HttpEntity(headers);
                        responseItem = restTemplate.exchange(builder.buildAndExpand(uriParams).toUri(), HttpMethod.GET, request, com.example.matth.finalapp.objects.MenuItem[].class);
                        List<com.example.matth.finalapp.objects.MenuItem> list = new ArrayList<>();
                        for(com.example.matth.finalapp.objects.MenuItem item: responseItem.getBody()) {
                            list.add(item);
                        }
                        menuMap.put(category, list);
                    }
                } catch (HttpClientErrorException e){
                    if(e.getStatusCode() == HttpStatus.CONFLICT){
                        status = HttpStatus.CONFLICT;
                    }
                }catch(ResourceAccessException e){
                    status = HttpStatus.BAD_REQUEST;
                }
                responsArray.add(status);
                return responsArray;
            }

            @Override
            protected void onPostExecute(ArrayList<Object> objects) {
                if((HttpStatus) objects.get(0) == HttpStatus.OK){
                    fillList();
                }
                super.onPostExecute(objects);
            }
        }.execute();
    }
}
