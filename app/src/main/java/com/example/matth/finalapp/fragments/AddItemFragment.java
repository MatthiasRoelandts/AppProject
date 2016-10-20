package com.example.matth.finalapp.fragments;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.example.matth.finalapp.MenuActivity;
import com.example.matth.finalapp.R;
import com.example.matth.finalapp.objects.Itemcategory;
import com.example.matth.finalapp.objects.MenuItem;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class AddItemFragment extends Fragment implements View.OnClickListener{

    private EditText titleText;
    private EditText descriptionText;
    private EditText priceText;
    private Spinner categorySpinner;
    private EditText newCategoryText;
    private Button addButton;
    private HashMap<String, Integer> categoryMap;

    public AddItemFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_add_item, container, false);

        titleText = (EditText) rootView.findViewById(R.id.addItem_title);
        descriptionText = (EditText) rootView.findViewById(R.id.addItem_description);
        priceText = (EditText) rootView.findViewById(R.id.addItem_price);
        categorySpinner = (Spinner) rootView.findViewById(R.id.addItem_spinner);
        newCategoryText = (EditText) rootView.findViewById(R.id.addItem_new_category);
        addButton = (Button) rootView.findViewById(R.id.addItem_button);
        categoryMap = new HashMap<>();
        getItemCategories();

        categorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String text = parent.getSelectedItem().toString();
                TextInputLayout newCategory = (TextInputLayout) rootView.findViewById(R.id.addItem_LayoutCategory);
                if(text.equals("Create new category")) {
                    newCategory.setVisibility(View.VISIBLE);
                }
                else {
                    newCategory.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        view.findViewById(R.id.addItem_button).setOnClickListener(this);
    }

    public void attemptAddingItem(View view){

        // Reset errors.
        titleText.setError(null);
        descriptionText.setError(null);
        priceText.setError(null);
        newCategoryText.setError(null);

        // Store values at the time of the registration attempt.
        String title = titleText.getText().toString();
        String description = descriptionText.getText().toString();
        String price = priceText.getText().toString();
        String category = categorySpinner.getSelectedItem().toString();
        String newCategory = newCategoryText.getText().toString();

        boolean cancel = false;
        boolean createNewCategory = false;
        View focusView = null;

        //Check if the user entered everything
        if(TextUtils.isEmpty(title)){
            titleText.setError(getString(R.string.error_field_required));
            focusView = titleText;
            cancel = true;
        }
        if(TextUtils.isEmpty(description)){
            descriptionText.setError(getString(R.string.error_field_required));
            focusView = descriptionText;
            cancel = true;
        }
        if(TextUtils.isEmpty(price)){
            priceText.setError(getString(R.string.error_field_required));
            focusView = priceText;
            cancel = true;
        }
        if(category.equals("Create new category")) {
            if(TextUtils.isEmpty(newCategory)){
                newCategoryText.setError(getString(R.string.error_field_required));
                focusView = newCategoryText;
                cancel = true;
            } else {
                createNewCategory = true;
            }
        }
        if (cancel) {
            // There was an error
            focusView.requestFocus();
        } else if(createNewCategory) {
            createItemCategory();
        } else {
            addItem(-1);
        }
    }

    private void createItemCategory(){
        new AsyncTask<Void, Void, Object[]>(){
            Itemcategory itemcategory;
            Object[] objectArray = new Object[2];
            @Override
            protected void onPreExecute() {
                itemcategory = new Itemcategory();
                itemcategory.setName(newCategoryText.getText().toString());
                itemcategory.setRestaurant_id(((MenuActivity) getActivity()).getBusinessId());
            }

            @Override
            protected Object[] doInBackground(Void... params) {
                final String url = "http://10.0.2.2:8080/create/itemcategory";
                HttpStatus status = null;
                RestTemplate restTemplate = new RestTemplate();
                restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
                restTemplate.getMessageConverters().add(new StringHttpMessageConverter());
                ResponseEntity<Itemcategory> response = null;
                try {
                    final HttpHeaders headers = new HttpHeaders();
                    headers.add("Authorization", ((MenuActivity) getActivity()).getAuthToken());
                    HttpEntity<Itemcategory> request = new HttpEntity(itemcategory, headers);
                    response = restTemplate.exchange(url, HttpMethod.POST, request, Itemcategory.class);
                    status = response.getStatusCode();
                    //Log.e("Created category:", response.getBody().toString());
                    int id = ((Itemcategory) response.getBody()).getId();
                    //Log.e("Created category:", Integer.toString(id));
                    objectArray[1] = id;
                } catch (HttpClientErrorException e){
                    if(e.getStatusCode() == HttpStatus.CONFLICT){
                        status = HttpStatus.CONFLICT;
                    }
                }catch(ResourceAccessException e){
                    status = HttpStatus.BAD_REQUEST;
                }
                objectArray[0] = status;
                return objectArray;
            }

            @Override
            protected void onPostExecute(Object[] status) {
                if(status[0] == HttpStatus.ACCEPTED){
                    addItem((Integer) status[1]);
                }
                if(status[0] == HttpStatus.CONFLICT){
                    System.out.println("Very bad conflict");
                    newCategoryText.setError(getString(R.string.error_duplicate_category));
                }
            }
        }.execute();
    }

    private void addItem(final int id) {

        new AsyncTask<Void, Void, HttpStatus>() {
            MenuItem menuItem;

            @Override
            protected void onPreExecute() {
                menuItem = new MenuItem();
                menuItem.setTitle(titleText.getText().toString());
                menuItem.setDescription(descriptionText.getText().toString());
                menuItem.setPrice(Double.parseDouble(priceText.getText().toString()));
                String category = categorySpinner.getSelectedItem().toString();
                if(id != -1) {
                    menuItem.setItemCategoryId(id);
                } else {
                    menuItem.setItemCategoryId(categoryMap.get(category));
                }
            }

            @Override
            protected HttpStatus doInBackground(Void... params) {
                final String url = "http://10.0.2.2:8080/create/item";
                HttpStatus status = null;
                RestTemplate restTemplate = new RestTemplate();
                // Add the Jackson and String message converters
                restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
                restTemplate.getMessageConverters().add(new StringHttpMessageConverter());
                ResponseEntity response = null;
                try {
                    final HttpHeaders headers = new HttpHeaders();
                    headers.add("Authorization", ((MenuActivity) getActivity()).getAuthToken());
                    HttpEntity<MenuItem> request = new HttpEntity(menuItem, headers);
                    response = restTemplate.exchange(url, HttpMethod.POST, request, MenuItem.class);
                    status = response.getStatusCode();

                } catch (HttpClientErrorException e){
                    if(e.getStatusCode() == HttpStatus.CONFLICT){
                        status = HttpStatus.CONFLICT;
                    }
                }catch(ResourceAccessException e){
                    status = HttpStatus.BAD_REQUEST;
                }
                return status;
            }

            @Override
            protected void onPostExecute(HttpStatus status) {
                if(status == HttpStatus.ACCEPTED){
                    ((MenuActivity) getActivity()).onBackPressed();
                }
                super.onPostExecute(status);
            }
        }.execute();
    }

    public void fillCategorySpinner(List<Itemcategory> categories) {
        ArrayList<String> categoryString = new ArrayList<>();
        for(Itemcategory category : categories){
            categoryString.add(category.getName());
            categoryMap.put(category.getName(), category.getId());
        }
        categoryString.add("Create new category");
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, categoryString);
        categorySpinner.setAdapter(adapter);
    }

    private void getItemCategories() {
        new AsyncTask<Void, Void, ArrayList<Object>>() {
            ArrayList<Object> responsArray = new ArrayList<Object>();
            @Override
            protected ArrayList<Object> doInBackground(Void... params) {
                final String url = "http://10.0.2.2:8080/ItemCategory/all";
                HttpStatus status = null;
                RestTemplate restTemplate = new RestTemplate();
                // Add the Jackson and String message converters
                restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
                restTemplate.getMessageConverters().add(new StringHttpMessageConverter());
                ResponseEntity<Itemcategory[]> response = null;
                try {
                    final HttpHeaders headers = new HttpHeaders();
                    headers.add("Authorization", ((MenuActivity) getActivity()).getAuthToken());
                    HttpEntity request = new HttpEntity(headers);
                    response = restTemplate.exchange(url, HttpMethod.GET, request, Itemcategory[].class);
                    status = response.getStatusCode();
                    List<Itemcategory> list = new ArrayList<Itemcategory>();
                    for(Itemcategory category: response.getBody()) {
                        //Log.e("Category:", category.getName());
                        list.add(category);
                    }
                    responsArray.add(list);
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
                if((HttpStatus) objects.get(1) == HttpStatus.OK){
                    fillCategorySpinner((List<Itemcategory>) objects.get(0));
                }
                super.onPostExecute(objects);
            }
        }.execute();
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.addItem_button) {
            attemptAddingItem(v);
        }
    }
}
