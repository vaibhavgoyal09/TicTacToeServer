package com.vaibhav.util

sealed class ResultHelper<T>(val data: T? = null, val errorMessage: String? = null) {

    class Success<T>(data: T): ResultHelper<T>(data = data)

    class Failure<T>(errorMessage: String): ResultHelper<T>(errorMessage = errorMessage)
}
