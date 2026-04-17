package com.ecoride.app.data.api.models

import com.google.gson.annotations.SerializedName

data class LoginRequest(
    val email: String,
    val password: String
)

data class LoginResponse(
    val ok: Boolean,
    val message: String,
    @SerializedName("access_token") val accessToken: String,
    val role: String,
    val username: String
)

data class ApiError(
    val ok: Boolean,
    val error: String
)

data class RegisterRequest(
    val username: String,
    val email: String,
    val password: String
)

data class RegisterResponse(
    val ok: Boolean,
    val message: String
)
