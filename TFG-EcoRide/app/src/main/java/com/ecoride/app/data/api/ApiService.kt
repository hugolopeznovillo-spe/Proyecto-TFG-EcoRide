package com.ecoride.app.data.api

import com.ecoride.app.data.api.models.*
import retrofit2.Response
import retrofit2.http.*

interface ApiService {

    // ── Auth ──────────────────────────────────────────────────────────────────

    @POST("auth/login")
    suspend fun login(@Body request: LoginRequest): Response<LoginResponse>

    @POST("auth/register")
    suspend fun register(@Body request: RegisterRequest): Response<ApiMessage>

    @GET("users/me")
    suspend fun getMyProfile(): Response<UserProfile>

    // ── Vehicles ──────────────────────────────────────────────────────────────

    @GET("vehicles")
    suspend fun getVehicles(): Response<List<VehicleDto>>

    @GET("vehicles/{id}")
    suspend fun getVehicleById(@Path("id") id: String): Response<VehicleDto>

    // ── Rentals ───────────────────────────────────────────────────────────────

    @POST("rentals/start")
    suspend fun startRental(@Body request: StartRentalRequest): Response<StartRentalResponse>

    @PUT("rentals/end/{id}")
    suspend fun endRental(@Path("id") rentalId: String): Response<RentalDto>

    @GET("rentals/active")
    suspend fun getActiveRental(): Response<RentalDto>

    @GET("rentals/my-history")
    suspend fun getMyHistory(): Response<List<RentalDto>>
}
