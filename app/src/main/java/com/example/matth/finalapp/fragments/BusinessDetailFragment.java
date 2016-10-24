package com.example.matth.finalapp.fragments;


import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.matth.finalapp.MenuActivity;
import com.example.matth.finalapp.R;
import com.example.matth.finalapp.objects.Business;
import com.example.matth.finalapp.objects.MenuItem;
import com.google.android.gms.vision.text.Text;
import com.google.gson.Gson;

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


/**
 * A simple {@link Fragment} subclass.
 */
public class BusinessDetailFragment extends BusinessBaseFragment {

    Business business;
    FragmentTransaction fragmentTransaction;
    Button save;
    Button delete;

    public BusinessDetailFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_business_detail, container, false);
        String businessString = getArguments().getString("business");
        Gson gson = new Gson();
        business = gson.fromJson(businessString, Business.class);
        //int id = getArguments().getInt("id");
        ((MenuActivity) getActivity()).getSupportActionBar().setTitle(business.getName());
        name = ((EditText) rootView.findViewById(R.id.business_name));
        address = (EditText) rootView.findViewById(R.id.business_address);
        city = (EditText) rootView.findViewById(R.id.business_city);
        postal = (EditText) rootView.findViewById(R.id.business_postal);
        description = (EditText) rootView.findViewById(R.id.business_description);
        save = (Button) rootView.findViewById(R.id.action_save_business);
        delete = (Button) rootView.findViewById(R.id.action_delete_business);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkForm() == false) {
                    saveBusiness();
                    ((MenuActivity) getActivity()).onBackPressed();
                }
            }
        });
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog diaBox = AskDelete();
                diaBox.show();
            }
        });
        name.setText(business.getName());
        address.setText(business.getAddress());
        city.setText(business.getCity());
        postal.setText(Integer.toString(business.getPostal()));
        description.setText(business.getInfo());

        return rootView;
    }

    public boolean compareBusiness() {
        boolean changed = false;
        if (name.getText().toString().equals(business.getName()) == false) {
            changed = true;
            Log.e("Name", "" + name.getText().toString() + " == " + business.getName());
        }
        if (address.getText().toString().equals(business.getAddress()) == false) {
            changed = true;
            Log.e("Address", "" + address.getText().toString() + " == " + business.getAddress());
        }
        if (city.getText().toString().equals(business.getCity()) == false) {
            changed = true;
            Log.e("City", "" + city.getText().toString() + " == " + business.getCity());
        }
        /*if(postal.getText().toString().equals(Integer.toString(business.getPostal())) == false) {
            changed = true;
        }*/
        if (description.getText().toString().equals(business.getInfo()) == false) {
            changed = true;
            Log.e("Info", "" + description.getText().toString() + " == " + business.getInfo());
        }
        return changed;
    }

    public void saveBusiness() {
        new AsyncTask<Void, Void, HttpStatus>() {
            Business newBusiness;

            @Override
            protected void onPreExecute() {
                newBusiness = new Business();
            }

            @Override
            protected HttpStatus doInBackground(Void... params) {
                final String url = "http://10.0.2.2:8080/business/update";
                HttpStatus status = null;
                RestTemplate restTemplate = new RestTemplate();
                // Add the Jackson and String message converters
                restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
                restTemplate.getMessageConverters().add(new StringHttpMessageConverter());
                ResponseEntity response = null;
                try {
                    final HttpHeaders headers = new HttpHeaders();
                    headers.add("Authorization", ((MenuActivity) getActivity()).getAuthToken());
                    HttpEntity<Business> request = new HttpEntity(newBusiness, headers);
                    response = restTemplate.exchange(url, HttpMethod.POST, request, Business.class);
                    status = response.getStatusCode();

                } catch (HttpClientErrorException e) {
                    if (e.getStatusCode() == HttpStatus.CONFLICT) {
                        status = HttpStatus.CONFLICT;
                    }
                } catch (ResourceAccessException e) {
                    status = HttpStatus.BAD_REQUEST;
                }
                return status;
            }

            @Override
            protected void onPostExecute(HttpStatus status) {
                if (status == HttpStatus.ACCEPTED) {
                    //((MenuActivity) getActivity()).onBackPressed();
                    Toast toast = Toast.makeText(getContext(), "Saved!", Toast.LENGTH_SHORT);
                    toast.show();
                }
                super.onPostExecute(status);
            }
        }.execute();
    }

    public void deleteBusiness() {
        new AsyncTask<Void, Void, HttpStatus>() {

            @Override
            protected HttpStatus doInBackground(Void... params) {
                final String url = "http://10.0.2.2:8080/business/delete";
                HttpStatus status = null;
                RestTemplate restTemplate = new RestTemplate();
                // Add the Jackson and String message converters
                restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
                restTemplate.getMessageConverters().add(new StringHttpMessageConverter());
                ResponseEntity response = null;
                try {
                    final HttpHeaders headers = new HttpHeaders();
                    headers.add("Authorization", ((MenuActivity) getActivity()).getAuthToken());
                    HttpEntity<Business> request = new HttpEntity(business, headers);
                    response = restTemplate.exchange(url, HttpMethod.POST, request, Business.class);
                    status = response.getStatusCode();

                } catch (HttpClientErrorException e) {
                    if (e.getStatusCode() == HttpStatus.CONFLICT) {
                        status = HttpStatus.CONFLICT;
                    }
                } catch (ResourceAccessException e) {
                    status = HttpStatus.BAD_REQUEST;
                }
                return status;
            }

            @Override
            protected void onPostExecute(HttpStatus status) {
                if (status == HttpStatus.ACCEPTED) {
                    ((MenuActivity) getActivity()).onBackPressed();
                    Toast toast = Toast.makeText(getContext(), "Deleted!", Toast.LENGTH_SHORT);
                    toast.show();
                }
                super.onPostExecute(status);
            }
        }.execute();
    }

    @Override
    public void onStop() {
        if (compareBusiness() == true) {
            AlertDialog diabox = AskSave();
            diabox.show();
        }
        super.onStop();
    }

    private AlertDialog AskDelete() {
        AlertDialog myQuittingDialogBox = new AlertDialog.Builder(getActivity())
                //set message, title, and icon
                .setTitle("Delete")
                .setMessage("Do you want to Delete?")
                .setIcon(R.drawable.ic_delete_black_24dp)

                .setPositiveButton("Delete", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int whichButton) {
                        //your deleting code
                        deleteBusiness();
                        dialog.dismiss();
                    }
                })

                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .create();
        return myQuittingDialogBox;
    }

    private AlertDialog AskSave() {
        AlertDialog myQuittingDialogBox = new AlertDialog.Builder(getActivity())
                //set message, title, and icon
                .setTitle("Save")
                .setMessage("Do you want to Save?")
                .setIcon(R.drawable.ic_account_balance_black_24dp)

                .setPositiveButton("Save", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int whichButton) {
                        //your saving code
                        saveBusiness();
                        dialog.dismiss();
                    }
                })

                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .create();
        return myQuittingDialogBox;
    }
}
