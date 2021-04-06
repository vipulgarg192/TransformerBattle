package com.app.transformerbattle.presenter.ui

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.transformerbattle.presenter.utils.TransformerEvents
import com.app.transformerbattle.repository.Repository
import com.app.transformerbattle.utils.Status
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject
constructor(private val mainRepository: Repository):  ViewModel() {
    private var _Token: MutableLiveData<Status<String>> = MutableLiveData()

    val Token: LiveData<Status<String>>
        get() = _Token

    fun onTriggerEvent(event: TransformerEvents){
        viewModelScope.launch {
            try {
                when(event){
                    is TransformerEvents.GetToken -> handleSuccess()
                }
            }catch (e: Exception){
                Log.e("TAG", "launchJob: Exception: ${e}, ${e.cause}")
                e.printStackTrace()
            }
        }
    }

    private suspend fun handleSuccess() {

        mainRepository.getToken()
                .onEach { event ->
                    _Token.postValue(event)
                }
                .launchIn(viewModelScope)
    }

    override fun onCleared() {
        super.onCleared()
    }
}
