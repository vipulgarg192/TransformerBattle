package com.app.transformerbattle.di

import com.app.transformerbattle.network.APIWorker
import com.app.transformerbattle.network.ApiService
import com.app.transformerbattle.network.Url
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppDependencies {

    @Provides
    @Singleton
    fun provideGsonConverterFactory(): GsonConverterFactory =
        APIWorker.gsonConverter

    @Provides
    @Singleton
    fun provideOkHttp(): OkHttpClient =
        APIWorker.client

    @Provides
    @Singleton
    fun provideRetrofitClient(
        gsonConverterFactory: GsonConverterFactory, okHttpClient: OkHttpClient
    ):  Retrofit.Builder {
        return  Retrofit.Builder()
            .baseUrl(Url.baseURL)
            .addConverterFactory(gsonConverterFactory)
            .client(okHttpClient)
    }

    @Singleton
    @Provides
    fun provideBlogService(retrofit: Retrofit.Builder): ApiService{
        return retrofit
            .build()
            .create(ApiService::class.java)
    }

}