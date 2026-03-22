package com.foodie.foodvisit.Activity

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.foodie.foodvisit.BuildConfig
import com.foodie.foodvisit.R
import com.foodie.foodvisit.fragment.AboutusFragment
import com.foodie.foodvisit.fragment.MainFragment
import com.foodie.foodvisit.fragment.MapsFragment
import com.foodie.foodvisit.fragment.ToVisitFragment
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.google.android.material.navigation.NavigationView
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    companion object {
        private const val TAG = "MainActivity"
        private const val KEY_FRAGMENT_TAG = "KEY"
    }

    private lateinit var navigationView: NavigationView
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var toolbar: Toolbar
    private lateinit var adview: AdView
    private var fragmentTag: String = MainFragment.TAG

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        navigationView = findViewById(R.id.nav_view)
        drawerLayout = findViewById(R.id.drawer_layout)
        toolbar = findViewById(R.id.toolbar)
        adview = findViewById(R.id.adView_main)

        if (savedInstanceState != null) {
            val fragment = supportFragmentManager
                .findFragmentByTag(savedInstanceState.getString(KEY_FRAGMENT_TAG))
            if (fragment != null) {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.generic_fragment, fragment)
                    .addToBackStack(null)
                    .commit()
            }
        } else {
            supportFragmentManager.beginTransaction()
                .replace(R.id.generic_fragment, MainFragment(), MainFragment.TAG)
                .addToBackStack(null)
                .commit()
        }

        setSupportActionBar(toolbar)
        val toggle = ActionBarDrawerToggle(
            this, drawerLayout, toolbar,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        )
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
        navigationView.setNavigationItemSelectedListener(this)

        initialiseAdView()
    }

    private fun initialiseAdView() {
        if ("free".equals(BuildConfig.FLAVOR, ignoreCase = true)) {
            val adRequest = AdRequest.Builder().build()
            adview.loadAd(adRequest)
        } else {
            adview.visibility = android.view.View.GONE
        }
    }

    override fun onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.action_settings) {
            startActivity(Intent(this, SettingsActivity::class.java))
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        val fragment: Fragment
        when (item.itemId) {
            R.id.nav_home -> {
                fragment = MainFragment()
                fragmentTag = MainFragment.TAG
            }
            R.id.nav_maps -> {
                fragment = MapsFragment()
                fragmentTag = MapsFragment.TAG
            }
            R.id.nav_tovisit -> {
                fragment = ToVisitFragment()
                fragmentTag = ToVisitFragment.TAG
            }
            R.id.nav_about_us -> {
                fragment = AboutusFragment()
                fragmentTag = AboutusFragment.TAG
            }
            R.id.nav_settings -> {
                startActivity(Intent(this, SettingsActivity::class.java))
                drawerLayout.closeDrawer(GravityCompat.START)
                return true
            }
            else -> return true
        }

        drawerLayout.closeDrawer(GravityCompat.START)
        supportFragmentManager.beginTransaction()
            .replace(R.id.generic_fragment, fragment, fragmentTag)
            .addToBackStack(null)
            .commit()
        return true
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(KEY_FRAGMENT_TAG, fragmentTag)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        fragmentTag = savedInstanceState.getString(KEY_FRAGMENT_TAG) ?: MainFragment.TAG
    }
}
