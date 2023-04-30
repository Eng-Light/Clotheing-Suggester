package com.nourelden515.clotheingsuggester.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.nourelden515.clotheingsuggester.data.Repository
import com.nourelden515.clotheingsuggester.ui.home.HomeViewModel
import com.nourelden515.clotheingsuggester.ui.location.LocationViewModel

class ViewModelFactory(private val repository: Repository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(HomeViewModel::class.java) ->
                HomeViewModel(repository) as T

            modelClass.isAssignableFrom(LocationViewModel::class.java) ->
                LocationViewModel(repository) as T

            else -> throw IllegalArgumentException("Unknown ViewModel class")
        }

    }
}