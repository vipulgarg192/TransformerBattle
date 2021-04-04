package com.app.transformerbattle.repository

import com.app.transformerbattle.domain.model.Transformer
import com.app.transformerbattle.domain.model.TransformerList
import com.app.transformerbattle.network.ApiService
import com.app.transformerbattle.network.model.TransformerDtoMapper
import com.app.transformerbattle.network.model.TransformerListDto

class AppRepository_Imple (private val apiService: ApiService, private val transformerDtoMapper: TransformerDtoMapper) : AppRepository {

    override suspend fun getTokenAppRepo(): String {
        return apiService.getToken()
    }

    override suspend fun createTransformerAppRepo(
        token: String,
        contentType: String,
        body: Transformer
    ): Transformer {
        return apiService.createTransformer(token,body)
    }

    override suspend fun getTransformerList(token: String): TransformerListDto {
        return apiService.getTransformer(token)
    }

}