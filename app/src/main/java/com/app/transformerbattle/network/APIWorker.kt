package com.app.transformerbattle.network

import com.google.gson.GsonBuilder
import okhttp3.*
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.converter.gson.GsonConverterFactory
import java.security.KeyManagementException
import java.security.NoSuchAlgorithmException
import java.util.concurrent.TimeUnit

object APIWorker {
    private var mClient: OkHttpClient?= null
    private var mGsonConverter: GsonConverterFactory?= null

    val client: OkHttpClient
        @Throws(NoSuchAlgorithmException::class, KeyManagementException::class)
        get() {
           if (mClient==null){
               val interceptor = HttpLoggingInterceptor()
               interceptor.level = HttpLoggingInterceptor.Level.BODY
               val httpBuilder = OkHttpClient.Builder()
               httpBuilder
                   .connectTimeout(15,TimeUnit.SECONDS)
                   .readTimeout(20,TimeUnit.SECONDS)
                   .addInterceptor(interceptor)

               httpBuilder.addInterceptor { chain ->
                   val newRequest = chain.request().newBuilder()
                       .addHeader(
                           "Content-Type", "application/json"
                       )
                       .build()
                   chain.proceed(newRequest)
               }
               mClient = httpBuilder.build()
           }
            return mClient!!
        }

    val gsonConverter: GsonConverterFactory
        get() {
           if (mGsonConverter == null){
               mGsonConverter = GsonConverterFactory
                   .create(
                       GsonBuilder()
                       .setLenient()
                       .disableHtmlEscaping()
                       .create())
           }
            return mGsonConverter!!
        }
}

