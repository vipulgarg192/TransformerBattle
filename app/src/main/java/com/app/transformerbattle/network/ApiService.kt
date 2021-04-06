package com.app.transformerbattle.network

import com.app.transformerbattle.domain.model.Transformer
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

    @PUT("transformers")
    suspend fun updateTransformer(
            @Header("Authorization") token: String,
            @Body requestBody: Transformer
    ): Transformer

    @DELETE("transformers/{transformerid}")
    suspend fun deleteTransformer(
            @Header("Authorization") token: String,
            @Path("transformerid") value : String
    )

}