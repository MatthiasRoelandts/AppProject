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
import android.widget.Toast;

public class MenuActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    ActionBarDrawerToggle toggleLeft;
    DrawerLayout drawerLayout;
    Toolbar myToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
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
        /*MenuItem manageRestaurants = (MenuItem) findViewById(R.id.nav_restaurants);
        manageRestaurants.setVisible(false);
        this.invalidateOptionsMenu();*/
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.action_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(toggleLeft.onOptionsItemSelected(item)) {
            drawerLayout.closeDrawer(Gravity.RIGHT);
            return true;
        }
        if( item.getItemId() == R.id.shopping_cart) {
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
                Toast.makeText(MenuActivity.this, "restaurants", Toast.LENGTH_SHORT).show();
                break;
            case R.id.nav_settings:
                Toast.makeText(MenuActivity.this, "Settings", Toast.LENGTH_SHORT).show();
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
