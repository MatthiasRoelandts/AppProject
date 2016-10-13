package com.example.matth.finalapp;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

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
        android.support.v4.app.FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
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
                   /* Snackbar.make(v, "Token successfully removed", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                    */
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
            changeToWaitersFragment();
            drawerLayout.closeDrawers();
        }
        return super.onOptionsItemSelected(item);
    }

    public void closeLeftDrawer() {
        drawerLayout.closeDrawers();
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
        fragmentTransaction.commit();
        DrawerLayout dl = (DrawerLayout)  findViewById(R.id.drawerLayout);
        if(dl.isDrawerOpen(GravityCompat.START)) {
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
}
