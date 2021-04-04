package com.app.transformerbattle.repository

import com.app.transformerbattle.domain.model.Transformer
import com.app.transformerbattle.domain.model.TransformerList
import com.app.transformerbattle.network.model.TransformerListDto

interface AppRepository {

    suspend fun getTokenAppRepo(): String

    suspend fun createTransformerAppRepo(token: String, contentType: String, body: Transformer): Transformer

    suspend fun getTransformerList(token: String): TransformerListDto

}