package com.ecoride.app.navigation

sealed class Routes(val route: String) {
    object Login      : Routes("login")
    object Register   : Routes("register")
    object VehicleList: Routes("vehicle_list")
    object VehicleDetail : Routes("vehicle_detail/{vehicleId}") {
        fun createRoute(vehicleId: String) = "vehicle_detail/$vehicleId"
    }
    object RentalHistory : Routes("rental_history")
}
