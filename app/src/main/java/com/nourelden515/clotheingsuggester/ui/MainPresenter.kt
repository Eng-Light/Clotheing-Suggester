package com.nourelden515.clotheingsuggester.ui

import com.nourelden515.clotheingsuggester.data.RepositoryImpl

class MainPresenter(
    private val repository: RepositoryImpl,
    private val mainView: MainView
) {
    fun getLatLon() {
        mainView.navigateToHome(repository.getLatLon())
    }
}