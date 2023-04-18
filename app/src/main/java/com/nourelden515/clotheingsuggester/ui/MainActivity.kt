package com.nourelden515.clotheingsuggester.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.nourelden515.clotheingsuggester.R
import com.nourelden515.clotheingsuggester.data.Repository
import com.nourelden515.clotheingsuggester.data.RepositoryImpl
import com.nourelden515.clotheingsuggester.data.source.RemoteDataSourceImpl
import com.nourelden515.clotheingsuggester.databinding.ActivityMainBinding
import com.nourelden515.clotheingsuggester.ui.home.HomeFragment
import com.nourelden515.clotheingsuggester.ui.location.LocationFragment
import com.nourelden515.clotheingsuggester.utils.shared.SharedPreferencesInterface
import com.nourelden515.clotheingsuggester.utils.shared.SharedPreferencesUtils
import java.util.Calendar

class MainActivity : AppCompatActivity(), MainView {

    private val sharedPreferencesUtils: SharedPreferencesInterface by lazy {
        SharedPreferencesUtils(this)
    }

    private val repository: Repository by lazy {
        RepositoryImpl(
            RemoteDataSourceImpl(),
            sharedPreferencesUtils
        )
    }
    private val presenter by lazy {
        MainPresenter(
            repository, this
        )
    }

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setAppTheme()
        setContentView(binding.root)
        presenter.getLatLon()
    }

    private fun setAppTheme() {
        val hourOfDay = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
        if (hourOfDay in DAY_START..DAY_END) {
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

    companion object {
        private const val DAY_START = 6
        private const val DAY_END = 17
    }
}