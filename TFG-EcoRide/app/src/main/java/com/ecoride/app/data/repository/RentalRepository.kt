package com.ecoride.app.data.repository

import com.ecoride.app.data.api.RetrofitInstance
import com.ecoride.app.data.api.models.RentalDto
import com.ecoride.app.data.api.models.StartRentalRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class RentalRepository {

    private val api = RetrofitInstance.api

    suspend fun startRental(vehicleId: String): Result<String> =
        withContext(Dispatchers.IO) {
            runCatching {
                val response = api.startRental(StartRentalRequest(vehicleId))
                if (response.isSuccessful) response.body()!!.message
                else error("Error ${response.code()}: ${response.errorBody()?.string()}")
            }
        }

    suspend fun endRental(rentalId: String): Result<RentalDto> =
        withContext(Dispatchers.IO) {
            runCatching {
                val response = api.endRental(rentalId)
                if (response.isSuccessful) response.body()!!
                else error("Error ${response.code()}: ${response.errorBody()?.string()}")
            }
        }

    suspend fun getActiveRental(): Result<RentalDto?> =
        withContext(Dispatchers.IO) {
            runCatching {
                val response = api.getActiveRental()
                when {
                    response.isSuccessful          -> response.body()
                    response.code() == 404         -> null   // sin alquiler activo
                    else -> error("Error ${response.code()}")
                }
            }
        }

    suspend fun getMyHistory(): Result<List<RentalDto>> =
        withContext(Dispatchers.IO) {
            runCatching {
                val response = api.getMyHistory()
                if (response.isSuccessful) response.body()!!
                else error("Error ${response.code()}: ${response.errorBody()?.string()}")
            }
        }
}
