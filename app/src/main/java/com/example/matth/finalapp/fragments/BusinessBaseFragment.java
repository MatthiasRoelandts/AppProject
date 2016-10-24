package com.example.matth.finalapp.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.example.matth.finalapp.MenuActivity;
import com.example.matth.finalapp.R;
import com.example.matth.finalapp.objects.Business;

/**
 * A simple {@link Fragment} subclass.
 */
public class BusinessBaseFragment extends Fragment {

    protected EditText name;
    protected EditText address;
    protected EditText city;
    protected EditText postal;
    protected EditText description;

    public boolean checkForm(){
        // Reset errors.
        name.setError(null);
        address.setError(null);
        city.setError(null);
        postal.setError(null);

        // Store values at the time of the registration attempt.
        String businessName = name.getText().toString();
        String businessAddress = address.getText().toString();
        String businessCity = city.getText().toString();
        String businessPostal = postal.getText().toString();
        //description can be null
        String businessDescription = description.getText().toString();

        boolean cancel = false;
        View focusView = null;
        if (TextUtils.isEmpty(businessPostal)) {
            postal.setError(getString(R.string.error_field_required));
            focusView = postal;
            cancel = true;
        }
        if (TextUtils.isEmpty(businessCity)) {
            city.setError(getString(R.string.error_field_required));
            focusView = city;
            cancel = true;
        }
        if (TextUtils.isEmpty(businessAddress)) {
            address.setError(getString(R.string.error_field_required));
            focusView = address;
            cancel = true;
        }
        if (TextUtils.isEmpty(businessName)) {
            name.setError(getString(R.string.error_field_required));
            focusView = name;
            cancel = true;
        }
        if (cancel) {
            // There was an error
            // form field with an error.
            focusView.requestFocus();
        }
        return cancel;
    }
}
