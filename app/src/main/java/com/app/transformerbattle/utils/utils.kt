package com.app.transformerbattle.utils

import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.app.transformerbattle.network.model.TransformerListDto

fun View.hideKeyboard() {
    val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(windowToken, 0)
}

//inline fun <T> LiveData<T>.filter(crossinline filter: (T?) -> Boolean?): LiveData<T> {
//    return MediatorLiveData<T>().apply {
//        addSource(this@filter) {
//            if (filter(it)) {
//                this.value = it
//            }
//        }
//    }
//}

inline fun <T> LiveData<T>.filter(crossinline filter: (T?) -> Boolean): LiveData<T> {
    return MediatorLiveData<T>().apply {
        addSource(this@filter) {
            if (filter(it)) {
                this.value = it
            }
        }
    }
}