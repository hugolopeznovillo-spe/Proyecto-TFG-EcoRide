package com.ecoride.app.data.local

import androidx.room.*
import kotlinx.coroutines.flow.Flow

// ── Entity ────────────────────────────────────────────────────────────────────

@Entity(tableName = "vehicles")
data class VehicleEntity(
    @PrimaryKey val id: String,
    val model: String,
    val battery: Int,
    val location: String,
    val pricePerMin: Double,
    val status: String
)

// ── DAO ───────────────────────────────────────────────────────────────────────

@Dao
interface VehicleDao {

    @Query("SELECT * FROM vehicles ORDER BY model ASC")
    fun getAllVehicles(): Flow<List<VehicleEntity>>

    @Query("SELECT * FROM vehicles WHERE id = :id")
    suspend fun getVehicleById(id: String): VehicleEntity?

    @Upsert
    suspend fun upsertAll(vehicles: List<VehicleEntity>)

    @Query("DELETE FROM vehicles")
    suspend fun clearAll()
}
