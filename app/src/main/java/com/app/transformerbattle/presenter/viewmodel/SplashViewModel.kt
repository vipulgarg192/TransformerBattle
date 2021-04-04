package com.app.transformerbattle.presenter.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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

    fun setStateEvent(splashStateEvent: SplashStateEvent) {
        viewModelScope.launch {
            when (splashStateEvent) {
                is SplashStateEvent.GetToken -> {
                        mainRepository.getToken()
                            .onEach { splashState ->
                                _Token.postValue(splashState)
                            }
                            .launchIn(viewModelScope)
                }
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
    }
}
sealed class SplashStateEvent{
    object GetToken: SplashStateEvent()
    object MoveActivity: SplashStateEvent()
}