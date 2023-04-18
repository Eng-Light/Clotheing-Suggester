package com.nourelden515.clotheingsuggester.ui

import android.content.res.Configuration
import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.nourelden515.clotheingsuggester.R
import com.nourelden515.clotheingsuggester.data.RepositoryImpl
import com.nourelden515.clotheingsuggester.data.source.RemoteDataSourceImpl
import com.nourelden515.clotheingsuggester.databinding.ActivityMainBinding
import com.nourelden515.clotheingsuggester.ui.home.HomeFragment
import com.nourelden515.clotheingsuggester.ui.location.LocationFragment
import com.nourelden515.clotheingsuggester.utils.shared.SharedPreferencesUtils
import java.util.Calendar

class MainActivity : AppCompatActivity(), MainView {

    private val presenter by lazy {
        MainPresenter(
            RepositoryImpl(
                RemoteDataSourceImpl(),
                SharedPreferencesUtils(this)
            ), this
        )
    }

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setAppTheme()
        setContentView(binding.root)
        presenter.getLatLon()
        statusBarTheme()
    }

    private fun setAppTheme() {
        val hourOfDay = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
        if (hourOfDay in 6..17) {
            setTheme(R.style.Theme_ClotheingSuggester_Day)
        } else {
            setTheme(R.style.Theme_ClotheingSuggester_Night)
        }
    }

    override fun navigateToHome(location: Pair<Float?, Float?>) {
        if (location.first != 0F && location.second != 0F) {
            navigateToHomeFragment(Pair(location.first!!.toDouble(), location.second!!.toDouble()))
        } else {
            navigateToLocationFragment()
        }
    }

    private fun replaceFragment(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fragment_container, fragment)
        transaction.commit()
    }

    private fun navigateToHomeFragment(location: Pair<Double, Double>) {
        val homeFragment = HomeFragment.newInstance(location.first, location.second)
        replaceFragment(homeFragment)
    }

    private fun navigateToLocationFragment() {
        replaceFragment(LocationFragment())
    }

    @Suppress("DEPRECATION")
    private fun statusBarTheme() {
        window.decorView.systemUiVisibility =
            View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
        window.statusBarColor = Color.TRANSPARENT

        if (isDarkTheme()) {
            window.decorView.systemUiVisibility =
                window.decorView.systemUiVisibility and View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR.inv()
        } else {
            window.decorView.systemUiVisibility =
                window.decorView.systemUiVisibility or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        }
        val decorView = window.decorView
        decorView.systemUiVisibility =
            decorView.systemUiVisibility or View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR
    }

    private fun isDarkTheme(): Boolean {
        val currentNightMode = resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
        return currentNightMode == Configuration.UI_MODE_NIGHT_YES
    }
}