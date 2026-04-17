package com.ecoride.app.data.api

import com.ecoride.app.data.api.models.*
import retrofit2.Response
import retrofit2.http.*

interface ApiService {

    @POST("auth/login")
    suspend fun login(@Body request: LoginRequest): Response<LoginResponse>

    @POST("auth/register")
    suspend fun register(@Body request: RegisterRequest): Response<RegisterResponse>

    @GET("vehicles")
    suspend fun getVehicles(): Response<VehicleListResponse>

    @GET("vehicles/{id}")
    suspend fun getVehicleById(@Path("id") id: String): Response<VehicleDetailResponse>

    @POST("rentals/start")
    suspend fun startRental(@Body request: StartRentalRequest): Response<StartRentalResponse>

    @PUT("rentals/end/{id}")
    suspend fun endRental(@Path("id") rentalId: String): Response<RentalDto>

    @GET("rentals/active")
    suspend fun getActiveRental(): Response<RentalDto>

    @GET("rentals/my-history")
    suspend fun getMyHistory(): Response<RentalHistoryResponse>
}
