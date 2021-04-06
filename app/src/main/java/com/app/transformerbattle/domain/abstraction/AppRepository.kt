package com.app.transformerbattle.domain.abstraction

import com.app.transformerbattle.domain.model.Transformer
import com.app.transformerbattle.network.model.TransformerListDto

interface AppRepository {

    suspend fun getTokenAppRepo(): String

    suspend fun createTransformerAppRepo(token: String,  body: Transformer): Transformer

    suspend fun getTransformerList(token: String): TransformerListDto

    suspend fun updateTransformerAppRepo(token: String, contentType: String, body: Transformer): Transformer

    suspend fun deleteTransformerAppRepo(token: String, id: String)

}