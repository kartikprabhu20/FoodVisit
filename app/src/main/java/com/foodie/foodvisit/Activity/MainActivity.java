
package com.foodie.foodvisit.Activity;

import android.content.Intent;
import android.os.PersistableBundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.foodie.foodvisit.BuildConfig;
import com.foodie.foodvisit.fragment.AboutusFragment;
import com.foodie.foodvisit.fragment.MainFragment;
import com.foodie.foodvisit.fragment.MapsFragment;
import com.foodie.foodvisit.R;
import com.foodie.foodvisit.fragment.ToVisitFragment;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by kprabhu on 11/11/17.
 */

public class MainActivity extends AppCompatActivity implements  NavigationView.OnNavigationItemSelectedListener{

    private static final String TAG = "MainActivity";
    @BindView(R.id.nav_view)
    NavigationView navigationView;

    @BindView(R.id.drawer_layout)
    DrawerLayout drawerLayout;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.adView_main)
    AdView adview;
    private String fragmentTag = MainFragment.TAG;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);


        if (savedInstanceState != null) {
            Fragment fragment = getSupportFragmentManager().findFragmentByTag(savedInstanceState.getString("KEY"));
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.generic_fragment, fragment);
            transaction.addToBackStack(null);
            transaction.commit();
        } else {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.generic_fragment, new MainFragment(),MainFragment.TAG);
            transaction.addToBackStack(null);
            transaction.commit();
        }


        setSupportActionBar(toolbar);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
        
        initialiseAdView();
    }

    private void initialiseAdView() {

        if("free".equalsIgnoreCase(BuildConfig.FLAVOR)){
            AdRequest adRequest = new AdRequest.Builder()
                    .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                    .build();
            adview.loadAd(adRequest);
        }else {
            adview.setVisibility(View.GONE);
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
            return  true;
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        Fragment fragment = new MainFragment();// Default
        switch (id){
            case R.id.nav_home:
                fragment = new MainFragment();
                fragmentTag = MainFragment.TAG;
                break;
            case R.id.nav_maps:
                fragment = new MapsFragment();
                fragmentTag = MapsFragment.TAG;
                break;
            case R.id.nav_tovisit:
                fragment = new ToVisitFragment();
                fragmentTag = ToVisitFragment.TAG;
                break;
            case R.id.nav_about_us:
                fragment = new AboutusFragment();
                fragmentTag = AboutusFragment.TAG;
                break;
            case R.id.nav_settings:
                Intent intent = new Intent(this, SettingsActivity.class);
                startActivity(intent);
                return  true;

        }

        drawerLayout.closeDrawer(GravityCompat.START);

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.generic_fragment, fragment, fragmentTag);
        transaction.addToBackStack(null);
        transaction.commit();
        return true;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("KEY", fragmentTag);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        fragmentTag = savedInstanceState.getString("KEY");
    }
}
