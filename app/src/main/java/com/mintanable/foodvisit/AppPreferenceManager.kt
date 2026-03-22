package com.mintanable.foodvisit

import android.content.Context
import android.content.SharedPreferences
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AppPreferenceManager @Inject constructor(@ApplicationContext context: Context) {

    companion object {
        private const val PREF_FILE = "com.foodie.foodvisitr"
        private const val LOCATION = "LOCATION"
    }

    private val sharedPref: SharedPreferences =
        context.getSharedPreferences(PREF_FILE, Context.MODE_PRIVATE)

    fun setLocation(location: String) {
        sharedPref.edit().putString(LOCATION, location).apply()
    }

    fun getLocation(): String = sharedPref.getString(LOCATION, "4") ?: "4"

    fun clear() {
        sharedPref.edit().clear().apply()
    }
}
