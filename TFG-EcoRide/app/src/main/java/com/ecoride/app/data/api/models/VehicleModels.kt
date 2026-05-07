package com.ecoride.app.data.api.models

import com.google.gson.annotations.SerializedName

data class VehicleDto(
    @SerializedName("_id")      val id: String,
    val model: String,
    val battery: Int,
    val location: String,
    @SerializedName("price_per_min") val pricePerMin: Double,
    val status: String,            // "disponible" | "en_uso" | "mantenimiento"
    @SerializedName("created_at")   val createdAt: String? = null
)
