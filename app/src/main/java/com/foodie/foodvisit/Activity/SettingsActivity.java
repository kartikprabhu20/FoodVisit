package com.foodie.foodvisit.Activity;


import android.content.Intent;

import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;

import com.foodie.foodvisit.FoodVisitApp;
import com.foodie.foodvisit.PreferenceManager;
import com.foodie.foodvisit.R;


/**
 * Created by kprabhu on 11/14/17.
 */

public class SettingsActivity extends PreferenceActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getFragmentManager().beginTransaction()
                .replace(android.R.id.content,new PrefsFragment())
                .commit();
    }

    public static class PrefsFragment extends PreferenceFragment implements Preference.OnPreferenceChangeListener{

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.settings);
            bindPreferenceSummaryToValue(findPreference(getString(R.string.pref_place_key)));

        }

        private void bindPreferenceSummaryToValue(Preference preference) {
            preference.setOnPreferenceChangeListener(this);

            onPreferenceChange(preference,
                    android.preference.PreferenceManager
                            .getDefaultSharedPreferences(preference.getContext())
                            .getString(preference.getKey(), ""));
        }

        @Override
        public boolean onPreferenceChange(Preference preference, Object value) {
            String stringValue = value.toString();
            PreferenceManager preferenceManager = PreferenceManager.getInstance();
            if(!preferenceManager.getLocation().equalsIgnoreCase(stringValue)) {
                ListPreference listPreference = (ListPreference) preference;
                int prefIndex = listPreference.findIndexOfValue(stringValue);
                if (prefIndex >= 0) {
                    preference.setSummary(listPreference.getEntries()[prefIndex]);
                }
                PreferenceManager.getInstance().setLocation(stringValue);
                Intent intent = new Intent(FoodVisitApp.getAppContext(), MainActivity.class);
                startActivity(intent);
            }
            return true;
        }
    }
}
