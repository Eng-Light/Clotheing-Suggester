package com.nourelden515.clotheingsuggester.utils

import android.graphics.drawable.Drawable
import androidx.appcompat.content.res.AppCompatResources
import androidx.fragment.app.Fragment
import com.nourelden515.clotheingsuggester.R

fun Fragment.replaceFragment(fragment: Fragment) {
    val fragmentManager = requireActivity().supportFragmentManager
    val fragmentTransaction = fragmentManager.beginTransaction()
    fragmentTransaction.replace(R.id.fragment_container, fragment).addToBackStack(null)
    fragmentTransaction.commit()
}

fun Fragment.getSummerOutfits(): List<Drawable> {
    return listOf(
        AppCompatResources.getDrawable(requireContext(), R.drawable.summer_outfit1)!!,
        AppCompatResources.getDrawable(requireContext(), R.drawable.summer_outfit2)!!,
        AppCompatResources.getDrawable(requireContext(), R.drawable.summer_outfit3)!!,
        AppCompatResources.getDrawable(requireContext(), R.drawable.summer_outfit4)!!,
        AppCompatResources.getDrawable(requireContext(), R.drawable.summer_outfit4)!!,
        AppCompatResources.getDrawable(requireContext(), R.drawable.summer_outfit6)!!,
        AppCompatResources.getDrawable(requireContext(), R.drawable.summer_outfit7)!!,
        AppCompatResources.getDrawable(requireContext(), R.drawable.summer_outfit8)!!,
        AppCompatResources.getDrawable(requireContext(), R.drawable.summer_outfit9)!!,
        AppCompatResources.getDrawable(requireContext(), R.drawable.summer_outfit10)!!
    )
}

fun Fragment.getWinterOutfits(): List<Drawable> {
    return listOf(
        AppCompatResources.getDrawable(requireContext(), R.drawable.winter_outfit1)!!,
        AppCompatResources.getDrawable(requireContext(), R.drawable.winter_outfit2)!!,
        AppCompatResources.getDrawable(requireContext(), R.drawable.winter_outfit3)!!,
        AppCompatResources.getDrawable(requireContext(), R.drawable.winter_outfit4)!!,
        AppCompatResources.getDrawable(requireContext(), R.drawable.winter_outfit5)!!,
        AppCompatResources.getDrawable(requireContext(), R.drawable.winter_outfit6)!!,
        AppCompatResources.getDrawable(requireContext(), R.drawable.winter_outfit7)!!,
        AppCompatResources.getDrawable(requireContext(), R.drawable.winter_outfit8)!!,
        AppCompatResources.getDrawable(requireContext(), R.drawable.winter_outfit9)!!,
        AppCompatResources.getDrawable(requireContext(), R.drawable.winter_outfit10)!!
    )
}