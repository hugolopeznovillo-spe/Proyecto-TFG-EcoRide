package com.ecoride.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.*
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.ecoride.app.data.api.RetrofitInstance
import com.ecoride.app.data.datastore.TokenDataStore
import com.ecoride.app.data.local.AppDatabase
import com.ecoride.app.data.repository.AuthRepository
import com.ecoride.app.data.repository.RentalRepository
import com.ecoride.app.data.repository.VehicleRepository
import com.ecoride.app.navigation.NavGraph
import com.ecoride.app.navigation.Routes
import com.ecoride.app.ui.detail.VehicleDetailViewModel
import com.ecoride.app.ui.history.RentalHistoryViewModel
import com.ecoride.app.ui.login.LoginViewModel
import com.ecoride.app.ui.register.RegisterViewModel
import com.ecoride.app.ui.theme.EcoRideTheme
import com.ecoride.app.ui.vehicles.VehicleListViewModel

class MainActivity : ComponentActivity() {

    // Dependencias creadas una vez en la Activity
    private lateinit var tokenDataStore:    TokenDataStore
    private lateinit var authRepository:    AuthRepository
    private lateinit var vehicleRepository: VehicleRepository
    private lateinit var rentalRepository:  RentalRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Inicializar dependencias
        tokenDataStore    = TokenDataStore(applicationContext)
        authRepository    = AuthRepository(tokenDataStore)
        vehicleRepository = VehicleRepository(AppDatabase.getInstance(applicationContext).vehicleDao())
        rentalRepository  = RentalRepository()

        setContent {
            EcoRideTheme {
                // Leer el token guardado para decidir la pantalla inicial
                val savedToken by tokenDataStore.tokenFlow.collectAsStateWithLifecycle(initialValue = null)
                val startDestination = if (savedToken != null) {
                    // Restaurar el interceptor con el token guardado
                    RetrofitInstance.authInterceptor.setToken(savedToken)
                    Routes.VehicleList.route
                } else {
                    Routes.Login.route
                }

                val navController = rememberNavController()

                // Fábricas de ViewModels con dependencias inyectadas manualmente
                NavGraph(
                    navController    = navController,
                    startDestination = startDestination,
                    loginVmFactory       = { LoginViewModel(authRepository) },
                    registerVmFactory    = { RegisterViewModel(authRepository) },
                    vehicleListVmFactory = { VehicleListViewModel(vehicleRepository, authRepository) },
                    vehicleDetailVmFactory = { VehicleDetailViewModel(vehicleRepository, rentalRepository) },
                    rentalHistoryVmFactory = { RentalHistoryViewModel(rentalRepository) }
                )
            }
        }
    }
}
