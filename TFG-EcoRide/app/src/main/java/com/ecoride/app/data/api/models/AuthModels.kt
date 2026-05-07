package com.ecoride.app.data.api.models

import com.google.gson.annotations.SerializedName

// ── Auth ──────────────────────────────────────────────────────────────────────

data class LoginRequest(
    val email: String,
    val password: String
)

data class RegisterRequest(
    val username: String,
    val email: String,
    val password: String
)

data class LoginResponse(
    @SerializedName("access_token") val accessToken: String,
    val role: String,
    val username: String
)

data class UserProfile(
    @SerializedName("_id") val id: String,
    val username: String,
    val email: String,
    val role: String,
    val active: Boolean,
    @SerializedName("created_at") val createdAt: String
)

data class ApiMessage(
    val message: String? = null,
    val ok: Boolean? = null
)
