package com.example.socialmediavbsanalay.data.repository

sealed class ApiResponse<out T : Any> {

    data class Success<out T : Any>(val data: T) : ApiResponse<T>()

    data class Fail(val e: Exception) : ApiResponse<Nothing>()

    data class Loading<out T : Any>(val data: T? = null) : ApiResponse<T>()
}