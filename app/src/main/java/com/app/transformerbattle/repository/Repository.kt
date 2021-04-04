package com.app.transformerbattle.repository

import com.app.transformerbattle.network.ApiService
import com.app.transformerbattle.utils.Status
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class Repository @Inject constructor(private val apiService: ApiService) {

    suspend fun getToken(): Flow<Status<String>> = flow{
        emit(Status.Loading)
        try {
            val tokenString = apiService.getToken()
            emit(Status.Success(tokenString))
        }catch (e: Exception){
            emit(Status.Error(e))
        }
    }
}