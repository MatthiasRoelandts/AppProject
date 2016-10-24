package com.example.matth.finalapp.fragments.personnel;


import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.matth.finalapp.MenuActivity;
import com.example.matth.finalapp.R;
import com.example.matth.finalapp.RestCommon;
import com.example.matth.finalapp.objects.Business;
import com.example.matth.finalapp.objects.Itemcategory;
import com.example.matth.finalapp.objects.Personnel;

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
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * A fragment with a Google +1 button.
 */
public class WaitersFragment extends Fragment implements View.OnClickListener {

    ListView listView;
    List<Personnel> personnelList;

    public WaitersFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_waiters, container, false);
        setHasOptionsMenu(true);
        personnelList = new ArrayList<>();
        System.out.println("the business id is " + ((MenuActivity) getActivity()).getBusinessId());
        int business_id = ((MenuActivity) getActivity()).getBusinessId();
        new getPersonnel().execute(business_id);

        ((MenuActivity) getActivity()).getSupportActionBar().setTitle("Personnel");

        listView = (ListView) rootView.findViewById(R.id.waiter_list);
        rootView.findViewById(R.id.floating_add_personnel_button).setOnClickListener(this);

        return rootView;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.floating_add_personnel_button:
                ((MenuActivity) getActivity()).switchToFragmentSaveBack(new AddPersonnelFragment());

        }
    }

    public void setPersonnelList(List<Personnel> list){
        personnelList = list;

        PersonnelAdapter adapter = new PersonnelAdapter(getContext(), personnelList);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                String itemValue = personnelList.get(position).getName();
                ((MenuActivity) getActivity()).changeToWaiterDetailFragment(itemValue);
            }
        });
    }

    private class getPersonnel extends AsyncTask<Object, Void, List<Personnel>> {


        @Override
        protected List<Personnel> doInBackground(Object... params) {


            Integer business_id = (Integer) params[0];
            RestCommon restCommon = ((MenuActivity)getActivity()).getRestCommon();
            final String url = restCommon.getBaseUrl() + "/business/{id}/personnel";
            RestTemplate restTemplate = restCommon.getRestTemplate();
            ResponseEntity<Personnel[]> response = null;
            List<Personnel> list = null;
            try {
                HttpEntity request = new HttpEntity(restCommon.getHeaders());
                Map<String, Integer> uriParams = new HashMap<>();
                uriParams.put("id", business_id);
                UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url);
                response = restTemplate.exchange(builder.buildAndExpand(uriParams).toUri(), HttpMethod.GET, request, Personnel[].class);
                list = Arrays.asList((Personnel[]) response.getBody());

            } catch (HttpClientErrorException e) {
                if (e.getStatusCode() == HttpStatus.NOT_FOUND) {
                    list = null;
                }
            }
            return list;
        }

        @Override
        protected void onPostExecute(List<Personnel> list_personnel) {
            if(list_personnel == null){

                ((MenuActivity) getActivity()).makeToast("An error occured personnel list not found");
            }else{
                setPersonnelList(list_personnel);

            }

        }
    }
}