package com.app.transformerbattle.network

import com.app.transformerbattle.domain.model.Transformer
import com.app.transformerbattle.domain.model.TransformerList
import com.app.transformerbattle.network.model.TransformerDto
import com.app.transformerbattle.network.model.TransformerListDto
import retrofit2.http.*

interface ApiService {

    @GET("allspark")
    suspend fun getToken(): String

    @POST("transformers")
    suspend fun createTransformer(
        @Header("Authorization") token: String,
        @Body requestBody: Transformer
    ): Transformer

    @GET("transformers")
    suspend fun getTransformer(
        @Header("Authorization") token: String
    ): TransformerListDto

}