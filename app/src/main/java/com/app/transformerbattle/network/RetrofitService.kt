package com.app.transformerbattle.network

import retrofit2.Retrofit

object RetrofitService {
    private const val TAG = "ApiService"

    fun ApiCall() = Retrofit.Builder()
        .baseUrl(Url.baseURL)
        .addConverterFactory(APIWorker.gsonConverter)
        .client(APIWorker.client)
        .build()
        .create(ApiService::class.java)
    
}