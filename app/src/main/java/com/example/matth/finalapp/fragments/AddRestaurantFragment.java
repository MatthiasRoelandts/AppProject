package com.example.matth.finalapp.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.example.matth.finalapp.MenuActivity;
import com.example.matth.finalapp.R;
import com.example.matth.finalapp.objects.Business;
import com.example.matth.finalapp.objects.Owner;
import com.google.android.gms.tasks.RuntimeExecutionException;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link AddRestaurantFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link AddRestaurantFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddRestaurantFragment extends BusinessBaseFragment implements View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private String args;

    private OnFragmentInteractionListener mListener;

    public AddRestaurantFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AddRestaurantFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AddRestaurantFragment newInstance(String param1, String param2) {
        AddRestaurantFragment fragment = new AddRestaurantFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //set title in the action bar
        ((MenuActivity) getActivity()).getSupportActionBar().setTitle("Add Business");

        //get the arguments
        if (getArguments() != null) {
            args = getArguments().getString("parentpage");
        }

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_restaurant, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstances) {
        super.onViewCreated(view, savedInstances);
        view.findViewById(R.id.action_add_business).setOnClickListener(this);
        name = (EditText) view.findViewById(R.id.business_name);
        address = (EditText) view.findViewById(R.id.businaess_address);
        city = (EditText) view.findViewById(R.id.business_city);
        postal = (EditText) view.findViewById(R.id.business_postal);
        description = (EditText) view.findViewById(R.id.business_description);
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

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.action_add_business:
                attemptAddBusiness();
        }
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);

        //function returns to the restaurant fragment and shows the list of businesses
        public void loadListOfBusinesses();
    }

    public void attemptAddBusiness() {

        if(checkForm() == false) {
            String ownerEmail = ((MenuActivity) getActivity()).getUserEmail();
            String authToken = ((MenuActivity) getActivity()).getAuthToken();

            Business business = new Business(name.getText().toString(), address.getText().toString(), city.getText().toString(), Integer.parseInt(postal.getText().toString()));
            business.setInfo(description.getText().toString());
            business.setOwnerEmail(ownerEmail);
            new AddBusiness().execute(business, authToken);
        }
    }


    private class AddBusiness extends AsyncTask<Object, Void, HttpStatus> {

        @Override
        protected HttpStatus doInBackground(Object... params) {
            final String url = "http://10.0.2.2:8080/addbusiness";
            HttpStatus status = null;
            RestTemplate restTemplate = new RestTemplate();
            Business business = (Business) params[0];
            String token = (String) params[1];

            // Add the Jackson and String message converters
            restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
            restTemplate.getMessageConverters().add(new StringHttpMessageConverter());
            ResponseEntity<Business> response = null;
            HttpHeaders header = new HttpHeaders();
            header.add("Authorization", token);

            try {
                HttpEntity<Business> request = new HttpEntity(business, header);
                response = restTemplate.exchange(url, HttpMethod.POST, request, Business.class);

                //If adding your first restaurant set the id and name
                if(args == "login") {
                    ((MenuActivity) getActivity()).setBusinessId(response.getBody().getId());
                    //((MenuActivity) getActivity()).setBusinessName(response.getBody().getName());
                }
                status = response.getStatusCode();
                System.out.println("success");

            } catch (RestClientException e) {
                status = HttpStatus.BAD_REQUEST;
            }
            return status;
        }

        @Override
        public void onPostExecute(HttpStatus status) {

            /*When the restaurant is created return to the restaurant fragment where a list is shown*/
            if (status == HttpStatus.CREATED) {
                //If coming from login go to home restaurant otherwise return to restaurants page
                if(args == "login"){
                    System.out.println("coming from login");
                    ((MenuActivity) getActivity()).switchToFragment(new HomeMenuFragment());

                }else{
                    getFragmentManager().popBackStack();
                }
            }
            if(status == HttpStatus.BAD_REQUEST){
                ((MenuActivity) getActivity()).makeToast("Server response failed");
            }
        }
    }
}
