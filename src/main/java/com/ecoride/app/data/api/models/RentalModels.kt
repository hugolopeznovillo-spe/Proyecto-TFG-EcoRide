package com.ecoride.app.data.api.models

import com.google.gson.annotations.SerializedName

data class RentalDto(
    @SerializedName("_id")           val id: String? = null,
    @SerializedName("user_id")       val userId: String? = null,
    val username: String? = null,
    @SerializedName("vehicle_id")    val vehicleId: String? = null,
    @SerializedName("vehicle_model") val vehicleModel: String? = null,
    @SerializedName("price_per_min") val pricePerMin: Double? = null,
    @SerializedName("start_time")    val startTime: String? = null,
    @SerializedName("end_time")      val endTime: String? = null,
    @SerializedName("duration_min")  val durationMin: Double? = null,
    @SerializedName("total_cost")    val totalCost: Double? = null,
    val status: String? = null              // "activo" | "finalizado"
)

data class StartRentalRequest(
    @SerializedName("vehicle_id") val vehicleId: String
)

data class StartRentalResponse(
    val message: String,
    @SerializedName("rental_id") val rentalId: String
)

data class RentalHistoryResponse(
    val ok: Boolean,
    val rentals: List<RentalDto>
)

data class ActiveRentalResponse(
    val ok: Boolean,
    val rental: RentalDto?
)
