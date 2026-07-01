package com.diabetes.prediction

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.core.view.WindowCompat
import androidx.navigation.compose.rememberNavController
import com.diabetes.prediction.ui.navigation.DiabetesNavGraph
import com.diabetes.prediction.ui.theme.DiabetesPredictionTheme
import com.diabetes.prediction.viewmodel.PredictionViewModel

class MainActivity : ComponentActivity() {

    private val viewModel: PredictionViewModel by viewModels {
        PredictionViewModel.Factory(application)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Enable edge-to-edge rendering
        WindowCompat.setDecorFitsSystemWindows(window, false)

        setContent {
            DiabetesPredictionTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()
                    DiabetesNavGraph(
                        navController = navController,
                        viewModel     = viewModel
                    )
                }
            }
        }
    }
}
