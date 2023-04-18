package com.nourelden515.clotheingsuggester.utils

import android.graphics.drawable.Drawable
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.content.res.AppCompatResources
import androidx.fragment.app.Fragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
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

fun Fragment.onClickBackFromNavigation() {
    val callback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            showAlertDialog()
        }
    }
    requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)
    callback.isEnabled = true
}

fun Fragment.showAlertDialog() {
    MaterialAlertDialogBuilder(requireContext())
        .setTitle(getString(R.string.title))
        .setMessage(getString(R.string.confirm))
        .setPositiveButton(
            getString(R.string.exit)
        ) { _, _ ->
            activity?.moveTaskToBack(true)
            activity?.finish()
        }
        .setNegativeButton(
            getString(R.string.stay)
        ) { _, _ ->
            Snackbar.make(requireView(), getString(R.string.canceled), Snackbar.LENGTH_SHORT)
                .show()
        }.show()
}