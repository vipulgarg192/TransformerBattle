package com.app.transformerbattle.repository

import com.app.transformerbattle.domain.model.Transformer

interface AppRepository {

    suspend fun getTokenAppRepo(): String

    suspend fun createTransformerAppRepo(token: String, contentType: String, body: Transformer): Transformer

//    suspend fun getTransformerList(token: String): List<Transformer>

}