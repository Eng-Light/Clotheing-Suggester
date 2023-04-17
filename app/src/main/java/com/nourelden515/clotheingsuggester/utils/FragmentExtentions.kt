package com.nourelden515.clotheingsuggester.utils

import androidx.fragment.app.Fragment
import com.nourelden515.clotheingsuggester.R

fun Fragment.replaceFragment(fragment: Fragment) {
    val fragmentManager = requireActivity().supportFragmentManager
    val fragmentTransaction = fragmentManager.beginTransaction()
    fragmentTransaction.replace(R.id.fragment_container, fragment).addToBackStack(null)
    fragmentTransaction.commit()
}