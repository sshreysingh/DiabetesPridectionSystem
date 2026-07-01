package com.diabetes.prediction.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.diabetes.prediction.ui.screens.*
import com.diabetes.prediction.viewmodel.PredictionViewModel

sealed class Screen(val route: String) {
    object Splash  : Screen("splash")
    object Home    : Screen("home")
    object Input   : Screen("input")
    object Result  : Screen("result")
    object History : Screen("history")
}

@Composable
fun DiabetesNavGraph(
    navController: NavHostController,
    viewModel: PredictionViewModel
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Splash.route
    ) {

        composable(Screen.Splash.route) {
            SplashScreen(
                onSplashComplete = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Splash.route) { inclusive = true }
                    }
                }
            )
        }

        composable(Screen.Home.route) {
            HomeScreen(
                onStartPrediction = { navController.navigate(Screen.Input.route) },
                onViewHistory     = { navController.navigate(Screen.History.route) }
            )
        }

        composable(Screen.Input.route) {
            InputFormScreen(
                viewModel         = viewModel,
                onNavigateBack    = { navController.popBackStack() },
                onPredictionSuccess = { navController.navigate(Screen.Result.route) }
            )
        }

        composable(Screen.Result.route) {
            ResultScreen(
                viewModel = viewModel,
                onNewPrediction = {
                    viewModel.resetForm()
                    navController.navigate(Screen.Input.route) {
                        popUpTo(Screen.Input.route) { inclusive = true }
                    }
                },
                onViewHistory = {
                    navController.navigate(Screen.History.route) {
                        popUpTo(Screen.Home.route)
                    }
                },
                onNavigateHome = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Home.route) { inclusive = true }
                    }
                }
            )
        }

        composable(Screen.History.route) {
            HistoryScreen(
                viewModel      = viewModel,
                onNavigateBack = { navController.popBackStack() }
            )
        }
    }
}
