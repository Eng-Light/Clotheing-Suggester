package com.nourelden515.clotheingsuggester.ui

import com.nourelden515.clotheingsuggester.data.Repository

class MainPresenter(
    private val repository: Repository,
    private val mainView: MainView
) {
    fun getLatLon() {
        mainView.navigateToHome(repository.getLatLon())
    }
}