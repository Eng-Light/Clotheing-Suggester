package com.nourelden515.clotheingsuggester.utils

import android.view.View
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.google.android.material.imageview.ShapeableImageView
import com.google.android.material.textview.MaterialTextView
import com.nourelden515.clotheingsuggester.R
import com.nourelden515.clotheingsuggester.data.models.ApiState
import com.nourelden515.clotheingsuggester.data.models.WeatherResponse
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.Locale

@BindingAdapter(value = ["app:showWhenLoading"])
fun <T> showWhenLoading(view: View, state: ApiState<T>?) {
    if (state is ApiState.Loading) {
        view.visibility = View.VISIBLE
    } else {
        view.visibility = View.GONE
    }
}

@BindingAdapter(value = ["location"])
fun setLocation(textView: TextView, weatherData: WeatherResponse?) {
    if (weatherData != null) {
        "${weatherData.location.name}, ${weatherData.location.region}".also {
            textView.text = it
        }
    }
}

@BindingAdapter(value = ["icon"])
fun setIcon(imageView: ShapeableImageView, imageUrl: String?) {
    Glide
        .with(imageView.context)
        .load("https:/${imageUrl}")
        .into(imageView)
}

@BindingAdapter(value = ["text_temp"])
fun setTempText(textView: MaterialTextView, temp: Double?) {
    if (temp != null) {
        "${temp.toInt()}°C".also {
            textView.text = it
        }
    }
}

@BindingAdapter(value = ["text_feels_like_temp"])
fun setFeelsLikeTempText(textView: MaterialTextView, temp: Double?) {
    if (temp != null) {
        "Feels Like ${
            temp.toInt()
        }°C".also {
            textView.text = it
        }
    }
}

@BindingAdapter(value = ["time"])
fun setDateTime(textView: MaterialTextView, dateStr: String?) {
    if (dateStr != null) {
        val formatter =
            DateTimeFormatter.ofPattern(textView.context.getString(R.string.yyyy_mm_dd_h_mm))
        val dateTime = LocalDateTime.parse(dateStr, formatter)

        val dayOfWeek = dateTime.dayOfWeek.getDisplayName(TextStyle.SHORT, Locale.getDefault())
        val time =
            dateTime.format(DateTimeFormatter.ofPattern(textView.context.getString(R.string.h_mm_a)))

        "$dayOfWeek, $time".also { textView.text = it }
    } else {
        // Handle the case where dateStr is null
        textView.text = ""
    }
}