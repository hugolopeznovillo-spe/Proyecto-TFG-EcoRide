package com.ecoride.app.data.repository

import com.ecoride.app.data.api.RetrofitInstance
import com.ecoride.app.data.api.models.VehicleDto
import com.ecoride.app.data.local.VehicleDao
import com.ecoride.app.data.local.VehicleEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class VehicleRepository(private val vehicleDao: VehicleDao) {

    private val api = RetrofitInstance.api

    /** Devuelve el flujo local (Room) — siempre reactivo */
    fun getCachedVehicles(): Flow<List<VehicleEntity>> = vehicleDao.getAllVehicles()

    /**
     * Llama a la API y actualiza la caché local.
     * Lanza excepción si hay error de red.
     */
    suspend fun refreshVehicles(): Result<Unit> =
        withContext(Dispatchers.IO) {
            runCatching {
                val response = api.getVehicles()
                if (response.isSuccessful) {
                    val entities = response.body()!!.map { it.toEntity() }
                    vehicleDao.upsertAll(entities)
                } else {
                    error("Error ${response.code()}: ${response.errorBody()?.string()}")
                }
            }
        }

    suspend fun getVehicleDetail(id: String): Result<VehicleDto> =
        withContext(Dispatchers.IO) {
            runCatching {
                val response = api.getVehicleById(id)
                if (response.isSuccessful) response.body()!!
                else error("Error ${response.code()}: ${response.errorBody()?.string()}")
            }
        }
}

// ── Mapper ────────────────────────────────────────────────────────────────────

fun VehicleDto.toEntity() = VehicleEntity(
    id          = id,
    model       = model,
    battery     = battery,
    location    = location,
    pricePerMin = pricePerMin,
    status      = status
)

fun VehicleEntity.toDto() = VehicleDto(
    id          = id,
    model       = model,
    battery     = battery,
    location    = location,
    pricePerMin = pricePerMin,
    status      = status
)
