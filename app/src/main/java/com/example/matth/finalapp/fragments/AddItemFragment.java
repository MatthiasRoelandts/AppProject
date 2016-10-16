package com.example.matth.finalapp.fragments;


import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import com.example.matth.finalapp.R;
import com.example.matth.finalapp.objects.Itemcategory;

import org.springframework.http.HttpStatus;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class AddItemFragment extends Fragment {

    private EditText titleText;
    private EditText descriptionText;
    private EditText priceText;
    private Spinner categorySpinner;
    private EditText newCategoryText;

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

        ArrayList<String> categoryArray = new ArrayList<>();
        categoryArray.add("Chose category");
        categoryArray.add("Food");
        categoryArray.add("Create new category");
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, categoryArray);
        categorySpinner.setAdapter(adapter);
        categorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String text = parent.getSelectedItem().toString();
                TextInputLayout newCategory = (TextInputLayout) rootView.findViewById(R.id.addItem_LayoutCategory);
                if(text.equals("Create new category")) {
                    newCategory.setVisibility(View.VISIBLE);
                }
                else {
                    newCategory.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        return rootView;
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
        String newCategory = newCategoryText.getText().toString();

        boolean cancel = false;
        View focusView = null;

        //Check if the user entered a firstname and last name
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

        if (cancel) {
            // There was an error; don't attempt registration and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {

            addItem();
        }
    }

    private void addItem() {
        new AsyncTask<Void, Void, HttpStatus>() {
            @Override
            protected void onPreExecute() {

            }
            @Override
            protected HttpStatus doInBackground(Void... params) {
                return null;
            }
        }.execute();
    }

}
