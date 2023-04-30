package com.nourelden515.clotheingsuggester.data.source.local

import android.graphics.drawable.Drawable

interface LocalDataSource {
    fun getSummerOutfits(): List<Drawable>
    fun getWinterOutfits(): List<Drawable>
}