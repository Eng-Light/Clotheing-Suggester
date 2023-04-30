package com.nourelden515.clotheingsuggester.data.models

sealed class ApiState<out T> {
    object Loading : ApiState<Nothing>()
    data class Success<T>(val data: T) : ApiState<T>()
    data class Error(val message: Throwable) : ApiState<Nothing>()

    fun toData(): T? {
        return when (this) {
            is Success -> data
            else -> null
        }
    }
}
