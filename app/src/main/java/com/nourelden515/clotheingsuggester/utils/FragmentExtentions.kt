package com.nourelden515.clotheingsuggester.utils

import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.nourelden515.clotheingsuggester.R

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