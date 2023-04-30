package com.nourelden515.clotheingsuggester.data.source.local

import android.content.Context
import android.graphics.drawable.Drawable
import androidx.appcompat.content.res.AppCompatResources
import com.nourelden515.clotheingsuggester.R

class LocalDataSourceImpl(private val context: Context) : LocalDataSource {
    override fun getSummerOutfits(): List<Drawable> {
        return listOf(
            AppCompatResources.getDrawable(context, R.drawable.summer_outfit1)!!,
            AppCompatResources.getDrawable(context, R.drawable.summer_outfit2)!!,
            AppCompatResources.getDrawable(context, R.drawable.summer_outfit3)!!,
            AppCompatResources.getDrawable(context, R.drawable.summer_outfit4)!!,
            AppCompatResources.getDrawable(context, R.drawable.summer_outfit4)!!,
            AppCompatResources.getDrawable(context, R.drawable.summer_outfit6)!!,
            AppCompatResources.getDrawable(context, R.drawable.summer_outfit7)!!,
            AppCompatResources.getDrawable(context, R.drawable.summer_outfit8)!!,
            AppCompatResources.getDrawable(context, R.drawable.summer_outfit9)!!,
            AppCompatResources.getDrawable(context, R.drawable.summer_outfit10)!!
        )
    }

    override fun getWinterOutfits(): List<Drawable> {
        return listOf(
            AppCompatResources.getDrawable(context, R.drawable.winter_outfit1)!!,
            AppCompatResources.getDrawable(context, R.drawable.winter_outfit2)!!,
            AppCompatResources.getDrawable(context, R.drawable.winter_outfit3)!!,
            AppCompatResources.getDrawable(context, R.drawable.winter_outfit4)!!,
            AppCompatResources.getDrawable(context, R.drawable.winter_outfit5)!!,
            AppCompatResources.getDrawable(context, R.drawable.winter_outfit6)!!,
            AppCompatResources.getDrawable(context, R.drawable.winter_outfit7)!!,
            AppCompatResources.getDrawable(context, R.drawable.winter_outfit8)!!,
            AppCompatResources.getDrawable(context, R.drawable.winter_outfit9)!!,
            AppCompatResources.getDrawable(context, R.drawable.winter_outfit10)!!
        )
    }
}