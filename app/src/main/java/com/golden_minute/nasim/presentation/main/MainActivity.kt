package com.golden_minute.nasim.presentation.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.ui.graphics.Color
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.golden_minute.nasim.presentation.onboarding.WelcomeScreen
import com.golden_minute.nasim.presentation.utils.DestinationRoutes
import com.golden_minute.nasim.ui.theme.NasimTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge(
            navigationBarStyle = SystemBarStyle.dark(
                Color.Transparent.value.toInt()
            ),
            statusBarStyle = SystemBarStyle.dark(
                Color.Transparent.value.toInt()
            )
        )
        setContent {

            NasimTheme {
                val navController = rememberNavController()
                NavHost(navController = navController, startDestination = DestinationRoutes.WELCOME_SCREEN.route) {
                    composable(DestinationRoutes.HOME_SCREEN.route) {
                        HomePage()
                    }
                    composable(DestinationRoutes.WELCOME_SCREEN.route) {
                        WelcomeScreen()
                    }
                }
            }
        }
    }
}






