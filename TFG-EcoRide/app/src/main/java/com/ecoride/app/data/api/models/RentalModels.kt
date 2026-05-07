package com.ecoride.app.data.api.models

import com.google.gson.annotations.SerializedName

data class RentalDto(
    @SerializedName("_id")           val id: String,
    @SerializedName("user_id")       val userId: String? = null,
    val username: String? = null,
    @SerializedName("vehicle_id")    val vehicleId: String,
    @SerializedName("vehicle_model") val vehicleModel: String,
    @SerializedName("price_per_min") val pricePerMin: Double,
    @SerializedName("start_time")    val startTime: String,
    @SerializedName("end_time")      val endTime: String? = null,
    @SerializedName("duration_min")  val durationMin: Double? = null,
    @SerializedName("total_cost")    val totalCost: Double? = null,
    val status: String              // "activo" | "finalizado"
)

data class StartRentalRequest(
    @SerializedName("vehicle_id") val vehicleId: String
)

data class StartRentalResponse(
    val message: String,
    @SerializedName("rental_id") val rentalId: String
)
