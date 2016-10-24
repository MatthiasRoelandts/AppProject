package com.example.matth.finalapp.fragments.personnel;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;

import com.example.matth.finalapp.MenuActivity;
import com.example.matth.finalapp.R;
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

/**
 * Created by michael on 20/10/2016.
 */

public class AddPersonnelFragment extends Fragment {

    private EditText pFirstname;
    private EditText pLastname;
    private EditText pEmail;
    private EditText pPhonenumber;
    private CheckBox pOwnerRightsCheck;
    private EditText pJobdescription;
    private EditText pHourlyWage;
    private EditText pAddress;
    private EditText pCity;
    private EditText pPostal;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        final View rootView = inflater.inflate(R.layout.fragment_add_personnel, container, false);
        setHasOptionsMenu(true);
        //set title in the action bar
        ((MenuActivity) getActivity()).getSupportActionBar().setTitle("Add To Personnel");

        pFirstname = (EditText) rootView.findViewById(R.id.personnel_firstname);
        pLastname = (EditText) rootView.findViewById(R.id.personnel_lastname);
        pEmail = (EditText) rootView.findViewById(R.id.personnel_email);
        pPhonenumber = (EditText) rootView.findViewById(R.id.personnel_phone);
        pOwnerRightsCheck = (CheckBox) rootView.findViewById(R.id.personell_owner_rights);
        pJobdescription = (EditText) rootView.findViewById(R.id.personnel_job_description);
        pHourlyWage = (EditText) rootView.findViewById(R.id.personnel_wage);
        pAddress = (EditText) rootView.findViewById(R.id.personnel_address);
        pCity = (EditText) rootView.findViewById(R.id.personnel_city);
        pPostal = (EditText) rootView.findViewById(R.id.personnel_postal);

        return rootView;

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.check_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.check_button: {
                ((MenuActivity) getActivity()).makeToast("I clickeeed on da button");
                attemptAddToPersonnel();
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onResume() {
        super.onResume();
        ((MenuActivity) getActivity()).turnMenuOff();
    }

    @Override
    public void onStop() {
        super.onStop();
        ((MenuActivity) getActivity()).turnMenuOn();
        ((MenuActivity) getActivity()).hideKeyboardFrom(getContext(), getView());

    }

    private void attemptAddToPersonnel() {
        // Reset errors.
        pFirstname.setError(null);
        pLastname.setError(null);
        pEmail.setError(null);
        pPhonenumber.setError(null);
        pJobdescription.setError(null);

        // Store values at the time of the registration attempt.
        String firstname = pFirstname.getText().toString();
        String lastname = pLastname.getText().toString();
        String email = pEmail.getText().toString();
        String phone = pPhonenumber.getText().toString();
        String jobdescription = pJobdescription.getText().toString();

        //description can be null
        String description = pJobdescription.getText().toString();

        boolean cancel = false;
        View focusView = null;

        if (TextUtils.isEmpty(jobdescription)) {
            pJobdescription.setError(getString(R.string.error_field_required));
            focusView = pJobdescription;
            cancel = true;
        }


        if (TextUtils.isEmpty(phone)) {
            pPhonenumber.setError(getString(R.string.error_field_required));
            focusView = pPhonenumber;
            cancel = true;
        }


        if (TextUtils.isEmpty(email)) {
            pEmail.setError(getString(R.string.error_field_required));
            focusView = pEmail;
            cancel = true;
        } else if (!((MenuActivity) getActivity()).isEmailValid(email)) {
            pEmail.setError(getString(R.string.error_invalid_email));
            focusView = pEmail;
            cancel = true;
        }

        if (TextUtils.isEmpty(lastname)) {
            pLastname.setError(getString(R.string.error_field_required));
            focusView = pLastname;
            cancel = true;
        }

        if (TextUtils.isEmpty(firstname)) {
            pFirstname.setError(getString(R.string.error_field_required));
            focusView = pFirstname;
            cancel = true;
        }

        if (cancel) {
            // There was an error
            // form field with an error.
            focusView.requestFocus();
        } else {


            //The password will be generated on the server side
            Personnel personnelMember = new Personnel(email, "");
            personnelMember.setAddress(pAddress.getText().toString());
            personnelMember.setCity(pCity.getText().toString());
            String postal = pPostal.getText().toString();
            System.out.println("The postal is " + postal);
            int postalToInt = (postal.equals("")) ? 0 : Integer.parseInt(postal);
            personnelMember.setPostal(postalToInt);
            String hourly_wage = pHourlyWage.getText().toString();
            double hourly_wageToDouble = (hourly_wage.equals("")) ? 0 : Double.parseDouble(hourly_wage);
            personnelMember.setHourly_salary(hourly_wageToDouble);
            personnelMember.setJob_description(jobdescription);
            personnelMember.setPhone(phone);
            personnelMember.setEmail(email);
            personnelMember.setLastname(lastname);
            personnelMember.setName(firstname);
            personnelMember.setRestaurant_id(((MenuActivity)getActivity()).getBusinessId());

            new addToPersonell().execute(personnelMember);
        }

    }

    private class addToPersonell extends AsyncTask<Object, Object, HttpStatus> {


        @Override
        protected HttpStatus doInBackground(Object... params) {

            final String url = "http://10.0.2.2:8080/user/personnel";
            String authToken = ((MenuActivity) getActivity()).getAuthToken();
            Personnel personnelMember = (Personnel) params[0];
            ResponseEntity<Personnel> response = null;

            HttpStatus status = null;
            RestTemplate restTemplate = new RestTemplate();

            // Add the Jackson and String message converters
            restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
            restTemplate.getMessageConverters().add(new StringHttpMessageConverter());

            HttpHeaders header = new HttpHeaders();
            header.add("Authorization", authToken);


            try {
                HttpEntity<Personnel> request = new HttpEntity(personnelMember, header);
                response = restTemplate.exchange(url, HttpMethod.POST, request, Personnel.class);
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
        public void onPostExecute(HttpStatus status) {
            View focusView;
            /*When the restaurant is created return to the restaurant fragment where a list is shown*/
            if (status == HttpStatus.CREATED) {
                ((MenuActivity) getActivity()).switchToFragment(new WaitersFragment());
            }
            if(status == HttpStatus.CONFLICT){
                System.out.println("Very bad conflict");
                pFirstname.setError(null);
                pLastname.setError(null);
                pEmail.setError(null);
                pJobdescription.setError(null);
                pEmail.setError(getString(R.string.error_duplicate_email));
                focusView = pEmail;
                focusView.requestFocus();
            }
            if(status == HttpStatus.BAD_REQUEST){
                ((MenuActivity) getActivity()).makeToast("Server response failed");
            }
        }
    }
}
