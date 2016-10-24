package com.example.matth.finalapp.fragments;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
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
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class HomeMenuFragment extends Fragment {

    DrawerLayout drawerLayout;
    ExpandableListView expandableListView;
    ExpandableListAdapter expandableListAdapter;
    List<String> expandableListTitle;
    List<Itemcategory> itemcategoryList;
    List<MenuItem> menuItemList;
    HashMap<String, List<String>> expandableListDetail;
    HashMap<Itemcategory, List<com.example.matth.finalapp.objects.MenuItem>> menuMap;

    public HomeMenuFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_home_menu, container, false);
        setHasOptionsMenu(true);
        ((MenuActivity) getActivity()).getSupportActionBar().setTitle("Menu");
        drawerLayout = (DrawerLayout) rootView.findViewById(R.id.drawerLayout2);
        expandableListView = (ExpandableListView) rootView.findViewById(R.id.expandableMenu);

        getMenu();
        //getWholeMenu();
        return rootView;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.shopping_cart) {
            if (drawerLayout.isDrawerOpen(Gravity.RIGHT) == true) {
                drawerLayout.closeDrawer(Gravity.RIGHT);
            } else {
                drawerLayout.openDrawer(Gravity.RIGHT);
                ((MenuActivity) getActivity()).closeLeftDrawer();
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

    private void fillList() {
        //expandableListDetail = ExpandableMenuPump.getData();
        //expandableListTitle = new ArrayList<String>(expandableListDetail.keySet());
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
        /*expandableListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {

            @Override
            public void onGroupExpand(int groupPosition) {
                Toast.makeText(getContext(),
                        expandableListTitle.get(groupPosition) + " List Expanded.",
                        Toast.LENGTH_SHORT).show();
            }
        });
        expandableListView.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {

            @Override
            public void onGroupCollapse(int groupPosition) {
                Toast.makeText(getContext(),
                        expandableListTitle.get(groupPosition) + " List Collapsed.",
                        Toast.LENGTH_SHORT).show();

            }
        });*/
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
                        HttpEntity request = new HttpEntity(headers);
                        Map<String, Integer> uriParams = new HashMap<String, Integer>();
                        uriParams.put("id", category.getId());
                        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(urlItem);
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
                if(objects.get(0) == HttpStatus.OK){
                    fillList();
                }
                super.onPostExecute(objects);
            }
        }.execute();
    }

    private void getWholeMenu() {
        menuItemList = new ArrayList<>();
        new AsyncTask<Void, Void, ArrayList<Object>>() {
            ArrayList<Object> responsArray = new ArrayList<Object>();
            @Override
            protected ArrayList<Object> doInBackground(Void... params) {
                final String url = "http://10.0.2.2:8080/MenuItem/category/{id}";
                HttpStatus status = null;
                RestTemplate restTemplate = new RestTemplate();
                // Add the Jackson and String message converters
                restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
                restTemplate.getMessageConverters().add(new StringHttpMessageConverter());
                ResponseEntity<com.example.matth.finalapp.objects.MenuItem[]> response = null;
                try {
                    Map<String, String> uriParams = new HashMap<String, String>();
                    uriParams.put("id", "28");
                    UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url);
                            //.queryParam("id", 28);
                    final HttpHeaders headers = new HttpHeaders();
                    headers.add("Authorization", ((MenuActivity) getActivity()).getAuthToken());
                    int id = 28;
                    HttpEntity request = new HttpEntity(headers);
                    Log.e("LINK:", builder.buildAndExpand(uriParams).toUri().toString());
                    response = restTemplate.exchange(builder.buildAndExpand(uriParams).toUri(), HttpMethod.GET, request, com.example.matth.finalapp.objects.MenuItem[].class);
                    status = response.getStatusCode();
                    List<com.example.matth.finalapp.objects.MenuItem> list = new ArrayList<>();
                    for(com.example.matth.finalapp.objects.MenuItem menuItem: response.getBody()) {
                        Log.e("Category:", menuItem.getTitle());
                        list.add(menuItem);
                    }
                    //responsArray.add(list);
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
                    //fillCategorySpinner((List<Itemcategory>) objects.get(0));
                    //fillList();
                }
                super.onPostExecute(objects);
            }
        }.execute();
    }
}
