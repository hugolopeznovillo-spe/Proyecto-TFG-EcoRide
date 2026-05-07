package com.ecoride.app.data.repository

import com.ecoride.app.data.api.RetrofitInstance
import com.ecoride.app.data.api.models.LoginRequest
import com.ecoride.app.data.api.models.LoginResponse
import com.ecoride.app.data.api.models.RegisterRequest
import com.ecoride.app.data.datastore.TokenDataStore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AuthRepository(private val tokenDataStore: TokenDataStore) {

    private val api = RetrofitInstance.api

    suspend fun login(email: String, password: String): Result<LoginResponse> =
        withContext(Dispatchers.IO) {
            runCatching {
                val response = api.login(LoginRequest(email, password))
                if (response.isSuccessful) {
                    val body = response.body()!!
                    tokenDataStore.saveSession(body.accessToken, body.role, body.username)
                    RetrofitInstance.authInterceptor.setToken(body.accessToken)
                    body
                } else {
                    error("Error ${response.code()}: ${response.errorBody()?.string()}")
                }
            }
        }

    suspend fun register(username: String, email: String, password: String): Result<String> =
        withContext(Dispatchers.IO) {
            runCatching {
                val response = api.register(RegisterRequest(username, email, password))
                if (response.isSuccessful) {
                    response.body()?.message ?: "Registro exitoso"
                } else {
                    error("Error ${response.code()}: ${response.errorBody()?.string()}")
                }
            }
        }

    suspend fun logout() {
        tokenDataStore.clearSession()
        RetrofitInstance.authInterceptor.setToken(null)
    }

    fun tokenFlow() = tokenDataStore.tokenFlow
    fun roleFlow()  = tokenDataStore.roleFlow
    fun usernameFlow() = tokenDataStore.usernameFlow
}
