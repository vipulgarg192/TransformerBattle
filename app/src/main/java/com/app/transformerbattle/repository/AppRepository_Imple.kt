package com.app.transformerbattle.repository

import com.app.transformerbattle.domain.abstraction.AppRepository
import com.app.transformerbattle.domain.model.Transformer
import com.app.transformerbattle.network.ApiService
import com.app.transformerbattle.network.model.TransformerListDto
import retrofit2.Response
import javax.inject.Inject

class AppRepository_Imple @Inject constructor(private val apiService: ApiService) : AppRepository {

    override suspend fun getTokenAppRepo(): String {
        return apiService.getToken()
    }

    override suspend fun createTransformerAppRepo(token: String, body: Transformer): Transformer {
        return apiService.createTransformer(token,body)
    }

    override suspend fun getTransformerList(token: String): TransformerListDto {
        return apiService.getTransformer(token)
    }

    override suspend fun updateTransformerAppRepo(token: String, contentType: String, body: Transformer): Transformer {
        return apiService.updateTransformer(token,body)
    }

    override suspend fun deleteTransformerAppRepo(token: String,  id: String): Response<Void> {
        return apiService.deleteTransformer(token,id)
    }

}