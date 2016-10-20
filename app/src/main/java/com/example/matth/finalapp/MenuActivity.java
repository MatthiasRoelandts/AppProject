package com.example.matth.finalapp;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.matth.finalapp.fragments.AddRestaurantFragment;
import com.example.matth.finalapp.fragments.ChangeMenuFragment;
import com.example.matth.finalapp.fragments.HomeMenuFragment;
import com.example.matth.finalapp.fragments.KitchenFragment;
import com.example.matth.finalapp.fragments.OrdersFragment;
import com.example.matth.finalapp.fragments.ReservationFragment;
import com.example.matth.finalapp.fragments.RestaurantFragment;
import com.example.matth.finalapp.fragments.SettingsFragment;
import com.example.matth.finalapp.fragments.WaiterDetailFragment;
import com.example.matth.finalapp.fragments.WaitersFragment;
import com.example.matth.finalapp.objects.Business;
import com.example.matth.finalapp.objects.Owner;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.w3c.dom.Text;

import java.util.Arrays;
import java.util.List;

public class MenuActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener, AddRestaurantFragment.OnFragmentInteractionListener {

    ActionBarDrawerToggle toggleLeft;
    DrawerLayout drawerLayout;
    HomeMenuFragment homeMenuFragment;
    Toolbar myToolbar;
    android.support.v4.app.FragmentTransaction fragmentTransaction;

    TextView business_name;
    TextView user_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        //add toolbar
        myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        myToolbar.setTitleTextColor(Color.WHITE);
        myToolbar.setVisibility(View.INVISIBLE);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(true);

        //TODO based if the user is an owner or not this function changes
        myToolbar.setTitle("Menu");

        drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        toggleLeft = new ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close);
        drawerLayout.addDrawerListener(toggleLeft);
        toggleLeft.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        NavigationView navigationView = (NavigationView) findViewById(R.id.menuLeft);
        navigationView.setNavigationItemSelectedListener(this);

        final Activity menuActivity = this;

        //set business and username variables -> are in nav header
        View header=  navigationView.getHeaderView(0);
        business_name = (TextView) header.findViewById(R.id.header_business_name);
        user_name = (TextView) header.findViewById(R.id.header_user_name);
        user_name.setText(getUserEmail());

        getBusinessesAndRedirect();

    }

    /*
    *
    * If the owner has no restaurants or multiple restaurants the owner gets redirected to the add restaurants fragment
    * otherwise he gets redirected to the home page
    *
    *
    * */
    public void getBusinessesAndRedirect() {

        List<Business> businessList;

        new AsyncTask<Void, Object, List<Business>>() {

            @Override
            protected List<Business> doInBackground(Void... params) {
                final String url = "http://10.0.2.2:8080/getbusinesses";
                HttpStatus status = null;
                RestTemplate restTemplate = new RestTemplate();
                // Add the Jackson and String message converters
                restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
                restTemplate.getMessageConverters().add(new StringHttpMessageConverter());
                // Make the HTTP POST request, marshaling the request to JSON, and the response to a String

                ResponseEntity response = null;
                List<Business> list = null;
                String token = getAuthToken();
                HttpHeaders header = new HttpHeaders();
                header.add("Authorization", token);
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
                // redirection logic based on the businesses
                redirectOwnerLogic(list);
            }

        }.execute();

    }

    public void redirectOwnerLogic(List<Business> list) {
        int list_size = list.size();
        System.out.println("The size of the list is " + list_size);
        if (list == null) {

            makeToast("Server response failed");
        } else if (list_size == 0) {

            //redirect to add business fragment
            Bundle args = new Bundle();
            args.putString("parentpage", "login");
            switchToFragment(new AddRestaurantFragment(), args);
        } else if (list_size == 1) {

            //redirect to the business home page
            makeToast("Redirect to home page of restaurant");
            setBusinessName(list.get(0).getName());
            switchToFragment(new HomeMenuFragment());
        } else if (list_size > 1) {

            //check which business is set as favorite if not go to business fragment
            /*TODO add favorit field to the restaurant fragment list*/
            Bundle args = new Bundle();
            args.putString("parentpage", "login");
            switchToFragment(new RestaurantFragment(), args);
        }

        myToolbar.setVisibility(View.VISIBLE);
    }

    public void switchToFragment(Fragment fragment) {
        fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout, fragment);
        fragmentTransaction.commit();
    }

    public void switchToFragment(Fragment fragment, Bundle args) {
        fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragment.setArguments(args);
        fragmentTransaction.replace(R.id.frameLayout, fragment);
        fragmentTransaction.commit();
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (toggleLeft.onOptionsItemSelected(item)) {
            if (homeMenuFragment != null) {
                homeMenuFragment.closeRightDrawer();
            }
            return true;
        }
        if (item.getItemId() == android.R.id.home) {
            changeToWaitersFragment();
            drawerLayout.closeDrawers();
        }
        return super.onOptionsItemSelected(item);
    }

    public void closeLeftDrawer() {
        drawerLayout.closeDrawers();
    }

    public void setBusinessName(String businessName){
        this.business_name.setText(businessName);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        fragmentTransaction = getSupportFragmentManager().beginTransaction();
        homeMenuFragment = null;
        switch (item.getItemId()) {
            case R.id.nav_home:
                homeMenuFragment = new HomeMenuFragment();
                fragmentTransaction.replace(R.id.frameLayout, homeMenuFragment);
                break;
            case R.id.nav_restaurants:
                RestaurantFragment restaurantFragment = new RestaurantFragment();
                fragmentTransaction.replace(R.id.frameLayout, restaurantFragment);
                break;
            case R.id.nav_settings:
                SettingsFragment settingsFragment = new SettingsFragment();
                fragmentTransaction.replace(R.id.frameLayout, settingsFragment);
                break;
            case R.id.nav_waiters:
                WaitersFragment waitersFragment = new WaitersFragment();
                fragmentTransaction.replace(R.id.frameLayout, waitersFragment);
                break;
            case R.id.nav_reservation:
                ReservationFragment reservationFragment = new ReservationFragment();
                fragmentTransaction.replace(R.id.frameLayout, reservationFragment);
                break;
            case R.id.nav_kitchen:
                KitchenFragment kitchenFragment = new KitchenFragment();
                fragmentTransaction.replace(R.id.frameLayout, kitchenFragment);
                break;
            case R.id.nav_orders:
                OrdersFragment ordersFragment = new OrdersFragment();
                fragmentTransaction.replace(R.id.frameLayout, ordersFragment);
                break;
            case R.id.nav_change_menu:
                ChangeMenuFragment changeMenuFragment = new ChangeMenuFragment();
                fragmentTransaction.replace(R.id.frameLayout, changeMenuFragment);
                break;
        }
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
        DrawerLayout dl = (DrawerLayout) findViewById(R.id.drawerLayout);
        if (dl.isDrawerOpen(GravityCompat.START)) {
            dl.closeDrawer(GravityCompat.START);
        }
        return false;
    }


    public void changeToWaiterDetailFragment(String name) {
        toggleLeft.setDrawerIndicatorEnabled(false);
        toggleLeft.syncState();
        Bundle bundle = new Bundle();
        bundle.putString("name", name);
        WaiterDetailFragment waiterDetail = new WaiterDetailFragment();
        waiterDetail.setArguments(bundle);
        fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout, waiterDetail);
        fragmentTransaction.commit();
    }

    public void changeToWaitersFragment() {
        toggleLeft.setDrawerIndicatorEnabled(true);
        toggleLeft.syncState();
        fragmentTransaction = getSupportFragmentManager().beginTransaction();
        WaitersFragment waitersFragment = new WaitersFragment();
        fragmentTransaction.replace(R.id.frameLayout, waitersFragment);
        fragmentTransaction.commit();
    }

    public void lockDrawer() {
        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
    }

    public void unlockDrawer() {
        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
    }

    public void hideToggle() {

    }

    public void redirectToLogin(){
        /*Snackbar.make(v, "Token successfully removed", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();*/
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public void loadListOfBusinesses() {

        fragmentTransaction = getSupportFragmentManager().beginTransaction();
        RestaurantFragment restaurantFragment = new RestaurantFragment();
        Bundle args = new Bundle();
        args.putString(RestaurantFragment.DATA_RECEIVE, "showList");
        restaurantFragment.setArguments(args);
        fragmentTransaction.replace(R.id.frameLayout, restaurantFragment);
        fragmentTransaction.commit();

    }

}
