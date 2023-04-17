package com.nourelden515.clotheingsuggester.utils

import android.content.Context
import android.content.SharedPreferences

class SharedPreferencesUtils(context: Context) {
    private var sharedPreferences: SharedPreferences? = null

    init {
        sharedPreferences =
            context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    }

    fun saveLatLon(lat: Float, lon: Float) {
        val editor: SharedPreferences.Editor? = sharedPreferences?.edit()
        clearLatLon()
        editor?.putFloat(LAT_KEY, lat)
        editor?.putFloat(LON_KEY, lon)
        editor?.apply()
    }

    fun getLatLon(): Pair<Float?, Float?> {
        val lat: Float? = sharedPreferences?.getFloat(LAT_KEY, 0F)
        val lon: Float? = sharedPreferences?.getFloat(LON_KEY, 0F)
        return Pair(lat, lon)
    }

    private fun clearLatLon() {
        val editor: SharedPreferences.Editor? = sharedPreferences?.edit()
        editor?.remove(LAT_KEY)
        editor?.remove(LON_KEY)
        editor?.apply()
    }

    fun saveImageIndex(newImageIndex: Int) {
        clearImageIndex()
        val editor: SharedPreferences.Editor? = sharedPreferences?.edit()
        editor?.putInt(IMAGE_KEY, newImageIndex)
        editor?.apply()
    }

    fun getImageIndex(): Int? {
        return sharedPreferences?.getInt(IMAGE_KEY, -1)
    }

    private fun clearImageIndex() {
        val editor: SharedPreferences.Editor? = sharedPreferences?.edit()
        editor?.remove(IMAGE_KEY)
        editor?.apply()
    }

    fun saveToday(today: Int) {
        clearDay()
        val editor: SharedPreferences.Editor? = sharedPreferences?.edit()
        editor?.putInt(DAY_KEY, today)
        editor?.apply()
    }

    fun getLastViewedDay(): Int? {
        return sharedPreferences?.getInt(DAY_KEY, -1)
    }

    private fun clearDay() {
        val editor: SharedPreferences.Editor? = sharedPreferences?.edit()
        editor?.remove(DAY_KEY)
        editor?.apply()
    }

    companion object {
        private const val PREFS_NAME = "MyPrefs"
        private const val LAT_KEY = "lat"
        private const val LON_KEY = "lon"
        private const val IMAGE_KEY = "imageIndex"
        private const val DAY_KEY = "Day"
    }
}