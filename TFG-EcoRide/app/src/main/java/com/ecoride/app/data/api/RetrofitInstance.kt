package com.ecoride.app.data.api

import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

// 10.0.2.2 = localhost del ordenador desde el emulador Android
private const val BASE_URL = "http://10.0.2.2:5000/"

/**
 * Interceptor que añade el JWT token a cada petición autenticada.
 * El token se actualiza mediante [setToken].
 */
class AuthInterceptor : Interceptor {
    private var token: String? = null

    fun setToken(newToken: String?) {
        token = newToken
    }

    override fun intercept(chain: Interceptor.Chain): okhttp3.Response {
        val original = chain.request()
        val request = token?.let {
            original.newBuilder()
                .addHeader("Authorization", "Bearer $it")
                .build()
        } ?: original
        return chain.proceed(request)
    }
}

object RetrofitInstance {

    val authInterceptor = AuthInterceptor()

    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(authInterceptor)
        .addInterceptor(loggingInterceptor)
        .build()

    val api: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }
}
