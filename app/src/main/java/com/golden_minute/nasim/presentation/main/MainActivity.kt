package com.golden_minute.nasim.presentation.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import coil.request.ImageRequest
import coil.size.Precision
import com.golden_minute.nasim.R
import com.golden_minute.nasim.data.data_store.CoordinateDataStore
import com.golden_minute.nasim.presentation.onboarding.WelcomeScreen
import com.golden_minute.nasim.presentation.search.SearchScreen
import com.golden_minute.nasim.presentation.search.SearchScreenViewModel
import com.golden_minute.nasim.presentation.utils.DestinationRoutes
import com.golden_minute.nasim.presentation.utils.getWeatherAppearance
import com.golden_minute.nasim.ui.theme.NasimTheme
import dagger.hilt.android.AndroidEntryPoint
import dev.chrisbanes.haze.haze
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

            val navController = rememberNavController()

            NasimTheme {

                Scaffold(
                    modifier = Modifier
                        .fillMaxSize()
                        .haze(viewModel.hazeStateForSystemBars),
                    bottomBar = {
                        AnimatedVisibility(
                            viewModel.lat != 0.0 && viewModel.lon != 0.0 && viewModel.weatherState.value != null && viewModel.contentIsLoaded.value,
                            enter = fadeIn(), exit = fadeOut()
                        ) {
                                BottomNavigationSection(
                                    viewModel = viewModel,
                                    hazeState = viewModel.hazeStateForNavigationBar,
                                    navController = navController,
                                    modifier = Modifier.padding(
                                        bottom = WindowInsets.navigationBars.asPaddingValues()
                                            .calculateBottomPadding(),
                                        top = 16.dp,
                                    )
                                )


                        }
                    }) { _ ->
                    val startDestination = if (viewModel.lat != 0.0 && viewModel.lon != 0.0)
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
                            viewModel.imageRequest = ImageRequest.Builder(LocalContext.current)
                                .data(viewModel.weatherState.value?.current?.condition?.let {
                                    viewModel.weatherState.value?.current?.isDay?.let { it1 ->
                                        getWeatherAppearance(
                                            it.code,
                                            it1
                                        )
                                    }
                                })
                                .precision(Precision.EXACT)
                                .build()
                            if (viewModel.weatherState.value == null)
                            viewModel.getWeather()
                            AnimatedContent(viewModel.isDisconnected, label = "") {
                                if (it.value.isBlank())
                                    HomePage(
                                        activityViewModel = viewModel,
                                        hazeStateForSystemBars = viewModel.hazeStateForSystemBars,
                                        hazeState = viewModel.hazeState,
                                        hazeStateForNavigationBar = viewModel.hazeStateForNavigationBar,
                                        navController = navController
                                    )
                                else
                                    Column(
                                        Modifier.fillMaxSize(),
                                        horizontalAlignment = Alignment.CenterHorizontally,
                                        verticalArrangement = Arrangement.spacedBy(
                                            24.dp,
                                            Alignment.CenterVertically
                                        )
                                    ) {
                                        Image(
                                            painter = painterResource(R.drawable.disconnected),
                                            contentDescription = "disconnected",
                                            modifier = Modifier.size(90.dp)
                                        )
                                        Text("No internet connection!", letterSpacing = 1.sp, fontWeight = FontWeight.Bold, color = Color.White, modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp), textAlign = TextAlign.Center, style = MaterialTheme.typography.headlineMedium)
                                        Text(
                                            viewModel.isDisconnected.value,
                                            fontWeight = FontWeight.Bold,
                                            letterSpacing = 1.sp,
                                            style = MaterialTheme.typography.headlineSmall,
                                            textAlign = TextAlign.Center,
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(horizontal = 24.dp)
                                        )
                                       OutlinedButton( modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp), shape = RoundedCornerShape(10.dp), onClick = {
                                           viewModel.getWeather()
                                       }) {
                                           Icon(Icons.Default.Refresh, contentDescription = "refresh")
                                           Spacer(Modifier.width(4.dp))
                                           Text("Reload", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.headlineSmall)
                                       }

                                    }
                            }


                        }

                        composable(route = DestinationRoutes.SEARCH_SCREEN.route) {

                            val searchScreenViewModel: SearchScreenViewModel = hiltViewModel()
                            SearchScreen(
                                searchScreenViewModel = searchScreenViewModel,
                                activityViewModel = viewModel
                            )
                        }
                        composable(route = DestinationRoutes.WELCOME_SCREEN.route) {
                            WelcomeScreen(navController = navController)

                        }

                    }
                }


            }


        }
    }
}







