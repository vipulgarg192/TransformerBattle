package com.app.transformerbattle.network

import retrofit2.Response
import retrofit2.http.GET

interface ApiService {

    @GET("allspark")
    suspend fun getToken(): Response<String>

}