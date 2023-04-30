package com.nourelden515.clotheingsuggester.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.nourelden515.clotheingsuggester.R
import com.nourelden515.clotheingsuggester.databinding.ActivityMainBinding
import java.util.Calendar

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setAppTheme()
        setContentView(binding.root)
    }

    private fun setAppTheme() {
        val hourOfDay = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
        if (hourOfDay in DAY_START..DAY_END) {
            setTheme(R.style.Theme_ClotheingSuggester_Day)
        } else {
            setTheme(R.style.Theme_ClotheingSuggester_Night)
        }
    }

    companion object {
        private const val DAY_START = 6
        private const val DAY_END = 17
    }
}