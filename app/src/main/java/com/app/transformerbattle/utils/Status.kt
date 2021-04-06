package com.app.transformerbattle.utils

sealed class Status<out R>
{
    data class Success<out T>(val data: T): Status<T>()
    data class Error(val exception: Exception): Status<Nothing>()
    object Loading: Status<Nothing>()
    object DoNothing: Status<Nothing>()

}
