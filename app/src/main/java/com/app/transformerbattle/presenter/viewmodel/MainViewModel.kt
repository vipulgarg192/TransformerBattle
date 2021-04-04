package com.app.transformerbattle.presenter.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.transformerbattle.domain.model.Transformer
import com.app.transformerbattle.domain.model.TransformerList
import com.app.transformerbattle.network.model.TransformerListDto
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

    private var _transformerList = MutableLiveData<TransformerListDto>()
    var transformerList: LiveData<TransformerListDto> = _transformerList

    fun onTriggerEvent(event: TransformerEvents){
        viewModelScope.launch {
            try {
                when(event){
                    is TransformerEvents.CreateTransformer -> createTransformer(event.transformer)  // this will create transformer
                    is TransformerEvents.GetTransformer -> getTransformer()

                }
            }catch (e: Exception){
                Log.e("TAG", "launchJob: Exception: ${e}, ${e.cause}")
                e.printStackTrace()
            }
        }
    }

    private suspend fun getTransformer() {
        _loading.postValue(true)
        val transformerList = appRepository.getTransformerList(
            token =  "Bearer ${appSharedPrefs.getStoredTag(AppSharedPrefs.tokenPref)}"
        )
        Log.e("TAG", "getTransformer: ${transformerList} ")

        _transformerList.postValue(transformerList)
        _loading.postValue(false)
    }

    private suspend fun createTransformer(transformer: Transformer) {
        _loading.postValue(true)
        val result = appRepository.createTransformerAppRepo(
            token =  "Bearer ${appSharedPrefs.getStoredTag(AppSharedPrefs.tokenPref)}",
            contentType = "application/json",
            body = transformer
        )
        _loading.postValue(false)
    }
}

