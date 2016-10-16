package com.example.matth.finalapp;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.matth.finalapp.fragments.AddItemFragment;
import com.example.matth.finalapp.fragments.ChangeMenuFragment;
import com.example.matth.finalapp.fragments.HomeMenuFragment;
import com.example.matth.finalapp.fragments.KitchenFragment;
import com.example.matth.finalapp.fragments.OrdersFragment;
import com.example.matth.finalapp.fragments.ReservationFragment;
import com.example.matth.finalapp.fragments.RestaurantFragment;
import com.example.matth.finalapp.fragments.SettingsFragment;
import com.example.matth.finalapp.fragments.WaiterDetailFragment;
import com.example.matth.finalapp.fragments.WaitersFragment;
import com.example.matth.finalapp.token.TokenManager;

public class MenuActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener{

    ActionBarDrawerToggle toggleLeft;
    DrawerLayout drawerLayout;
    HomeMenuFragment homeMenuFragment;
    Toolbar myToolbar;
    android.support.v4.app.FragmentTransaction fragmentTransaction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        //add toolbar
        myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        myToolbar.setTitle("Menu");
        myToolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(true);

        //set the main fragment
        homeMenuFragment = new HomeMenuFragment();
        fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout, homeMenuFragment);
        fragmentTransaction.commit();

        drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        toggleLeft = new ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close);
        drawerLayout.addDrawerListener(toggleLeft);
        toggleLeft.syncState();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        NavigationView navigationView = (NavigationView) findViewById(R.id.menuLeft);
        navigationView.setNavigationItemSelectedListener(this);

        final Activity menuActivity = this;

        Button logOutButton = (Button) findViewById(R.id.logOutButton);
        logOutButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {

                TokenManager tokenManager = new TokenManager(PreferenceManager.getDefaultSharedPreferences(menuActivity));
                setLoggedin(false);
                tokenManager.removeToken();
                if(getAuthToken()==""){
                    Intent intent = new Intent(menuActivity,LoginActivity.class  );
                    startActivity(intent);
                }
            }
        });

        //show only the right menu items
        /*MenuItem manageRestaurants = (MenuItem) findViewById(R.id.nav_restaurants);
        manageRestaurants.setVisible(false);
        this.invalidateOptionsMenu();*/
    }

    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.action_menu, menu);
        return true;
    }*/

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(toggleLeft.onOptionsItemSelected(item)) {
            if(homeMenuFragment !=null) {
                homeMenuFragment.closeRightDrawer();
            }
            return true;
        }
        if(item.getItemId() == android.R.id.home) {
            if(getSupportFragmentManager().getBackStackEntryCount() > 0) {
                getSupportFragmentManager().popBackStack();
                turnMenuOn();
            }
            drawerLayout.closeDrawers();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        Log.d("backpressed", "backstand = "+ getSupportFragmentManager().getBackStackEntryCount());
        if(getSupportFragmentManager().getBackStackEntryCount() > 0) {
            getSupportFragmentManager().popBackStack();
            turnMenuOn();
        } else {
            super.onBackPressed();
        }
    }

    public void closeLeftDrawer() {
        drawerLayout.closeDrawers();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
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
        DrawerLayout dl = (DrawerLayout)  findViewById(R.id.drawerLayout);
        if(dl.isDrawerOpen(GravityCompat.START)) {
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

    private void turnMenuOff() {
        toggleLeft.setDrawerIndicatorEnabled(false);
        toggleLeft.syncState();
    }

    private void turnMenuOn() {
        toggleLeft.setDrawerIndicatorEnabled(true);
        toggleLeft.syncState();
    }
}
