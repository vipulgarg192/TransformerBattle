package com.app.transformerbattle.presenter.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.transformerbattle.domain.model.Transformer
import com.app.transformerbattle.presenter.utils.TransformerEvents
import com.app.transformerbattle.repository.AppRepository
import com.app.transformerbattle.repository.AppSharedPrefs
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject
constructor(private val appRepository: AppRepository, private val appSharedPrefs: AppSharedPrefs): ViewModel() {

    private var _loading : MutableLiveData<Boolean> = MutableLiveData()
    val loading : LiveData<Boolean>
        get() = _loading

    fun onTriggerEvent(event: TransformerEvents){
        viewModelScope.launch {
            try {
                when(event){
                    is TransformerEvents.CreateTransformer -> {
                        Log.e("TAG", "createTransformer: "+appSharedPrefs.getStoredTag(AppSharedPrefs.tokenPref) )

                        createTransformer(event.transformer)
                    }
                }
            }catch (e: Exception){
                Log.e("TAG", "launchJob: Exception: ${e}, ${e.cause}")
                e.printStackTrace()
            }
        }
    }

    private suspend fun createTransformer(transformer: Transformer) {
        _loading.value = true
        Log.e("TAG", "createTransformer: "+appSharedPrefs.getStoredTag(AppSharedPrefs.tokenPref) )
        val result = appRepository.createTransformerAppRepo(
            token =  "Bearer ${appSharedPrefs.getStoredTag(AppSharedPrefs.tokenPref)}",
            contentType = "application/json",
            body = transformer
        )
        Log.e("TAG", "createTransformer: "+result )

        _loading.value = false
    }
}