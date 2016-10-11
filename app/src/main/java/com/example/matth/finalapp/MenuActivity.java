package com.example.matth.finalapp;

import android.graphics.Color;
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

import com.example.matth.finalapp.fragments.ChangeMenuFragment;
import com.example.matth.finalapp.fragments.HomeMenuFragment;
import com.example.matth.finalapp.fragments.KitchenFragment;
import com.example.matth.finalapp.fragments.OrdersFragment;
import com.example.matth.finalapp.fragments.ReservationFragment;
import com.example.matth.finalapp.fragments.RestaurantFragment;
import com.example.matth.finalapp.fragments.SettingsFragment;
import com.example.matth.finalapp.fragments.WaitersFragment;

public class MenuActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    ActionBarDrawerToggle toggleLeft;
    DrawerLayout drawerLayout;
    Toolbar myToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        //add toolbar
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        myToolbar.setTitle("Menu");
        myToolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(true);

        //set the main fragment
        HomeMenuFragment homeMenuFragment = new HomeMenuFragment();
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

        //show only the right menu items
        //navigationView.getMenu().clear(); //clear old inflated items.
        //navigationView.inflateMenu(R.menu.nav_waiter_menu);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.action_menu, menu);
        //MenuItem menuitem = (MenuItem) menu.findItem(R.id.shopping_cart);
        //menuitem.setVisible(false);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(toggleLeft.onOptionsItemSelected(item)) {
            drawerLayout.closeDrawer(Gravity.RIGHT);
            return true;
        }
        if(item.getItemId() == R.id.shopping_cart) {
            if(drawerLayout.isDrawerOpen(Gravity.RIGHT) == true) {
                drawerLayout.closeDrawer(Gravity.RIGHT);
            }
            else {
                drawerLayout.openDrawer(Gravity.RIGHT);
            }
            drawerLayout.closeDrawer(Gravity.LEFT);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        android.support.v4.app.FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        switch (item.getItemId()) {
            case R.id.nav_home:
                HomeMenuFragment homeMenuFragment = new HomeMenuFragment();
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
}
