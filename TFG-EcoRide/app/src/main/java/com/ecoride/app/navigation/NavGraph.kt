package com.ecoride.app.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.ecoride.app.ui.detail.VehicleDetailScreen
import com.ecoride.app.ui.detail.VehicleDetailViewModel
import com.ecoride.app.ui.history.RentalHistoryScreen
import com.ecoride.app.ui.history.RentalHistoryViewModel
import com.ecoride.app.ui.login.LoginScreen
import com.ecoride.app.ui.login.LoginViewModel
import com.ecoride.app.ui.register.RegisterScreen
import com.ecoride.app.ui.register.RegisterViewModel
import com.ecoride.app.ui.vehicles.VehicleListScreen
import com.ecoride.app.ui.vehicles.VehicleListViewModel

@Composable
fun NavGraph(
    navController: NavHostController,
    startDestination: String,
    loginVmFactory:          () -> LoginViewModel,
    registerVmFactory:       () -> RegisterViewModel,
    vehicleListVmFactory:    () -> VehicleListViewModel,
    vehicleDetailVmFactory:  () -> VehicleDetailViewModel,
    rentalHistoryVmFactory:  () -> RentalHistoryViewModel
) {
    NavHost(navController = navController, startDestination = startDestination) {

        composable(Routes.Login.route) {
            val vm: LoginViewModel = viewModel(factory = viewModelFactory(loginVmFactory))
            LoginScreen(
                viewModel      = vm,
                onLoginSuccess = {
                    navController.navigate(Routes.VehicleList.route) {
                        popUpTo(Routes.Login.route) { inclusive = true }
                    }
                },
                onGoToRegister = { navController.navigate(Routes.Register.route) }
            )
        }

        composable(Routes.Register.route) {
            val vm: RegisterViewModel = viewModel(factory = viewModelFactory(registerVmFactory))
            RegisterScreen(
                viewModel         = vm,
                onRegisterSuccess = { navController.popBackStack() },
                onBack            = { navController.popBackStack() }
            )
        }

        composable(Routes.VehicleList.route) {
            val vm: VehicleListViewModel = viewModel(factory = viewModelFactory(vehicleListVmFactory))
            VehicleListScreen(
                viewModel      = vm,
                onVehicleClick = { vehicleId ->
                    navController.navigate(Routes.VehicleDetail.createRoute(vehicleId))
                },
                onHistoryClick = { navController.navigate(Routes.RentalHistory.route) },
                onLogout = {
                    navController.navigate(Routes.Login.route) {
                        popUpTo(Routes.VehicleList.route) { inclusive = true }
                    }
                }
            )
        }

        composable(
            route     = Routes.VehicleDetail.route,
            arguments = listOf(navArgument("vehicleId") { type = NavType.StringType })
        ) { backStackEntry ->
            val vehicleId = backStackEntry.arguments?.getString("vehicleId") ?: return@composable
            val vm: VehicleDetailViewModel = viewModel(factory = viewModelFactory(vehicleDetailVmFactory))
            VehicleDetailScreen(
                vehicleId = vehicleId,
                viewModel = vm,
                onBack    = { navController.popBackStack() }
            )
        }

        composable(Routes.RentalHistory.route) {
            val vm: RentalHistoryViewModel = viewModel(factory = viewModelFactory(rentalHistoryVmFactory))
            RentalHistoryScreen(
                viewModel = vm,
                onBack    = { navController.popBackStack() }
            )
        }
    }
}

/** Helper para crear un ViewModelProvider.Factory desde un lambda */
@Suppress("UNCHECKED_CAST")
fun <VM : androidx.lifecycle.ViewModel> viewModelFactory(
    create: () -> VM
): androidx.lifecycle.ViewModelProvider.Factory =
    object : androidx.lifecycle.ViewModelProvider.Factory {
        override fun <T : androidx.lifecycle.ViewModel> create(modelClass: Class<T>): T =
            create() as T
    }
