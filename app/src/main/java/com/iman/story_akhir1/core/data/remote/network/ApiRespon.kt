package com.iman.story_akhir1.core.data.remote.network

sealed class ApiRespon<out R> {
    data class Success<out T>(val body: T) : ApiRespon<T>()
    data class Error(val errorMessage: String) : ApiRespon<Nothing>()
    object Empty : ApiRespon<Nothing>()
}