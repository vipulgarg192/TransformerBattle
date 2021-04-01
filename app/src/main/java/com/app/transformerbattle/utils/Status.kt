package com.app.transformerbattle.utils

sealed class Status{

    data class Success(var loading: Boolean) : Status()
    data class Loading(var loading: Boolean) : Status()
    data class Error(val e: Throwable) : Status()


    companion object {

        fun loading(isLoading: Boolean): Status = Loading(isLoading)
        fun success(isLoading: Boolean): Status = Success(isLoading)
        fun error(e: Throwable): Status = Error(e)

    }
}