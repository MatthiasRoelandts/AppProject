package com.example.matth.finalapp;

import android.app.Activity;
import android.content.ClipData;
import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import com.example.matth.finalapp.fragments.AddItemFragment;
import com.example.matth.finalapp.fragments.AddRestaurantFragment;
import com.example.matth.finalapp.fragments.ChangeMenuFragment;
import com.example.matth.finalapp.fragments.HomeMenuFragment;
import com.example.matth.finalapp.fragments.KitchenFragment;
import com.example.matth.finalapp.fragments.OrdersFragment;
import com.example.matth.finalapp.fragments.ReservationFragment;
import com.example.matth.finalapp.fragments.RestaurantFragment;
import com.example.matth.finalapp.fragments.SettingsFragment;
import com.example.matth.finalapp.fragments.personnel.WaiterDetailFragment;
import com.example.matth.finalapp.fragments.personnel.WaitersFragment;
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
    Menu menu;
    TextView business_name;
    TextView user_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        //add toolbar
        myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        myToolbar.setTitle("Menu");
        myToolbar.setTitleTextColor(Color.WHITE);
        myToolbar.setVisibility(View.INVISIBLE);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(true);

        //set the main fragment
        homeMenuFragment = new HomeMenuFragment();
        fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout, homeMenuFragment);
        fragmentTransaction.commit();
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
        View header = navigationView.getHeaderView(0);
        business_name = (TextView) header.findViewById(R.id.header_business_name);
        user_name = (TextView) header.findViewById(R.id.header_user_name);
        user_name.setText(getUserEmail());

        //get the navigation menu
        menu = navigationView.getMenu();
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


            //show only the right menu items
        /*MenuItem manageRestaurants = (MenuItem) findViewById(R.id.nav_restaurants);
        manageRestaurants.setVisible(false);
        this.invalidateOptionsMenu();*/
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
            setBusinessId(list.get(0).getId());
            ShowBusinessSettings(list.get(0));
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

    public void ShowBusinessSettings(Business business) {

        MenuItem kitchen = menu.findItem(R.id.nav_kitchen);
        MenuItem reservations = menu.findItem(R.id.nav_reservation);
        MenuItem personnel = menu.findItem(R.id.nav_waiters);
        MenuItem tables = menu.findItem(R.id.nav_tables);

        if (!business.isKitchen()) {
            kitchen.setVisible(false);
        }
        if (!business.isPersonnel()) {
            personnel.setVisible(false);
        }
        if (!business.isTables()) {
            tables.setVisible(false);
        }
        if (!business.isReservations()) {
            reservations.setVisible(false);
        }

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

    public void switchToFragmentSaveBack(Fragment fragment) {
        fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout, fragment);
        fragmentTransaction.addToBackStack(null);
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
            if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
                getSupportFragmentManager().popBackStack();
                turnMenuOn();
            }
            drawerLayout.closeDrawers();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        View view = this.getCurrentFocus();
        Log.d("backpressed", "backstand = " + getSupportFragmentManager().getBackStackEntryCount());

        if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
            getSupportFragmentManager().popBackStack();
            turnMenuOn();
        } else {
            super.onBackPressed();
        }
    }

    /*Hides the keyboard when pressing the back button returning to a fragment*/
    public static void hideKeyboardFrom(Context context, View view) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public void closeLeftDrawer() {
        drawerLayout.closeDrawers();
    }

    public void setBusinessName(String businessName) {
        this.business_name.setText(businessName);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        fragmentTransaction = getSupportFragmentManager().beginTransaction();
        homeMenuFragment = null;
        getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        switch (item.getItemId()) {
            case R.id.nav_home:
                turnMenuOn();
                fragmentTransaction = getSupportFragmentManager().beginTransaction();
                homeMenuFragment = new HomeMenuFragment();
                fragmentTransaction.replace(R.id.frameLayout, homeMenuFragment);
                fragmentTransaction.commit();
                break;
            case R.id.nav_restaurants:
                turnMenuOn();
                fragmentTransaction = getSupportFragmentManager().beginTransaction();
                RestaurantFragment restaurantFragment = new RestaurantFragment();
                fragmentTransaction.replace(R.id.frameLayout, restaurantFragment);
                fragmentTransaction.commit();
                break;
            case R.id.nav_settings:
                turnMenuOn();
                fragmentTransaction = getSupportFragmentManager().beginTransaction();
                SettingsFragment settingsFragment = new SettingsFragment();
                fragmentTransaction.replace(R.id.frameLayout, settingsFragment);
                fragmentTransaction.commit();
                break;
            case R.id.nav_waiters:
                changeToWaitersFragment();
                break;
            case R.id.nav_reservation:
                turnMenuOn();
                fragmentTransaction = getSupportFragmentManager().beginTransaction();
                ReservationFragment reservationFragment = new ReservationFragment();
                fragmentTransaction.replace(R.id.frameLayout, reservationFragment);
                fragmentTransaction.commit();
                break;
            case R.id.nav_kitchen:
                turnMenuOn();
                fragmentTransaction = getSupportFragmentManager().beginTransaction();
                KitchenFragment kitchenFragment = new KitchenFragment();
                fragmentTransaction.replace(R.id.frameLayout, kitchenFragment);
                fragmentTransaction.commit();
                break;
            case R.id.nav_orders:
                turnMenuOn();
                fragmentTransaction = getSupportFragmentManager().beginTransaction();
                OrdersFragment ordersFragment = new OrdersFragment();
                fragmentTransaction.replace(R.id.frameLayout, ordersFragment);
                fragmentTransaction.commit();
                break;
            case R.id.nav_change_menu:
                changeToChangeMenuFragment();
                break;
        }
        DrawerLayout dl = (DrawerLayout) findViewById(R.id.drawerLayout);
        if (dl.isDrawerOpen(GravityCompat.START)) {
            dl.closeDrawer(GravityCompat.START);
        }
        return false;
    }


    public void changeToWaiterDetailFragment(String name) {
        turnMenuOff();
        Bundle bundle = new Bundle();
        bundle.putString("name", name);
        WaiterDetailFragment waiterDetail = new WaiterDetailFragment();
        waiterDetail.setArguments(bundle);
        fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout, waiterDetail);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    public void changeToWaitersFragment() {
        turnMenuOn();
        fragmentTransaction = getSupportFragmentManager().beginTransaction();
        WaitersFragment waitersFragment = new WaitersFragment();
        fragmentTransaction.replace(R.id.frameLayout, waitersFragment);
        fragmentTransaction.commit();
    }

    public void changeToChangeMenuFragment() {
        turnMenuOn();
        fragmentTransaction = getSupportFragmentManager().beginTransaction();
        ChangeMenuFragment changeMenuFragment = new ChangeMenuFragment();
        fragmentTransaction.replace(R.id.frameLayout, changeMenuFragment);
        fragmentTransaction.commit();
    }

    public void changeToAddItemFragment() {
        turnMenuOff();
        AddItemFragment addItemFragment = new AddItemFragment();
        fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout, addItemFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }


    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public void loadListOfBusinesses() {

    }


    public void turnMenuOff() {
        toggleLeft.setDrawerIndicatorEnabled(false);
        toggleLeft.syncState();
    }

    public void turnMenuOn() {
        toggleLeft.setDrawerIndicatorEnabled(true);
        toggleLeft.syncState();
    }


    public void lockDrawer() {
        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
    }

    public void unlockDrawer() {
        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
    }
}
