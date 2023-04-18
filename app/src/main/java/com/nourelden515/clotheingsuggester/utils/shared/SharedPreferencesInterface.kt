package com.nourelden515.clotheingsuggester.utils.shared

import android.content.SharedPreferences

/**
 *This Name is just to not duplicate the naming of SharedPreferences existing Interface
 * @see SharedPreferences existing Interface
 */
interface SharedPreferencesInterface {
    fun saveLatLon(lat: Float, lon: Float)
    fun getLatLon(): Pair<Float?, Float?>
    fun clearLatLon()
    fun saveImageIndex(newImageIndex: Int)
    fun getImageIndex(): Int?
    fun clearImageIndex()
    fun saveToday(today: Int)
    fun getLastViewedDay(): Int?
    fun clearDay()
}