package com.nourelden515.clotheingsuggester.data.models

import com.google.gson.annotations.SerializedName

data class Condition(
    @SerializedName("text") val text: String,
    @SerializedName("icon") val iconUrl: String,
    @SerializedName("code") val code: Int
)
