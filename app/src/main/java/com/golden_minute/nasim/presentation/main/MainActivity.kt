package com.golden_minute.nasim.presentation.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.golden_minute.nasim.data.data_store.CoordinateDataStore
import com.golden_minute.nasim.presentation.onboarding.WelcomeScreen
import com.golden_minute.nasim.presentation.utils.DestinationRoutes
import com.golden_minute.nasim.ui.theme.NasimTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val viewModel: ActivityViewModel by viewModels()

    @Inject
    lateinit var dataStore: CoordinateDataStore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        installSplashScreen().apply {
            setKeepOnScreenCondition {
                !viewModel.turnOffSplashScreen
            }
        }

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

                val startDestination = if (viewModel.lat!= 0.0 && viewModel.lon != 0.0)
                    DestinationRoutes.HOME_SCREEN.route
                else
                    DestinationRoutes.WELCOME_SCREEN.route

                NavHost(
                    navController = navController,
                    startDestination = startDestination,
                    popEnterTransition = { fadeIn() },
                    popExitTransition = { fadeOut() },
                    enterTransition = { fadeIn() },
                    exitTransition = { fadeOut() }) {



                    composable(DestinationRoutes.HOME_SCREEN.route) {
                        val entry = remember {
                            navController.getBackStackEntry(DestinationRoutes.HOME_SCREEN.route)
                        }
                        val homeViewModel: HomeViewModel = hiltViewModel(entry)
                        HomePage(viewModel = homeViewModel)


                    }
                    composable(route = DestinationRoutes.WELCOME_SCREEN.route) {
                        WelcomeScreen(navController = navController)
                    }

                }


            }


        }
    }
}







