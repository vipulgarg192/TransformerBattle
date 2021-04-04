package com.app.transformerbattle.network

import com.app.transformerbattle.domain.model.Transformer
import com.app.transformerbattle.network.model.TransformerDto
import retrofit2.http.*

interface ApiService {

    @GET("allspark")
    suspend fun getToken(): String

    @POST("transformers")
    suspend fun createTransformer(
        @Header("Authorization") token: String,
        @Header("Content-Type") content: String,
        @Body requestBody: Transformer
    ): TransformerDto

}