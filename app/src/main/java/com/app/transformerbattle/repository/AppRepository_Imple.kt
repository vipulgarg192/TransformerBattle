package com.app.transformerbattle.repository

import com.app.transformerbattle.domain.model.Transformer
import com.app.transformerbattle.network.ApiService
import com.app.transformerbattle.network.model.TransformerDtoMapper

class AppRepository_Imple (private val apiService: ApiService, private val transformerDtoMapper: TransformerDtoMapper) : AppRepository {

    override suspend fun getTokenAppRepo(): String {
        return apiService.getToken()
    }

    override suspend fun createTransformerAppRepo(
        token: String,
        contentType: String,
        body: Transformer
    ): Transformer {
        return transformerDtoMapper.mapToDomainModel(apiService.createTransformer(token,contentType,body))
    }

}