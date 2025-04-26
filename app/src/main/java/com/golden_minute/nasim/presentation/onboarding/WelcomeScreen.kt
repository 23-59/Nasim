package com.golden_minute.nasim.presentation.onboarding

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.golden_minute.nasim.R
import com.golden_minute.nasim.presentation.main.ActivityViewModel
import com.golden_minute.nasim.presentation.utils.DestinationRoutes
import com.golden_minute.nasim.ui.theme.NasimTheme
import com.golden_minute.nasim.ui.theme.fontFamily
import dev.chrisbanes.haze.HazeState
import dev.chrisbanes.haze.HazeStyle
import dev.chrisbanes.haze.haze
import dev.chrisbanes.haze.hazeChild
import kotlinx.coroutines.launch


@Composable
fun WelcomeScreen(
    modifier: Modifier = Modifier,
    viewModel: WelcomeScreenViewModel = hiltViewModel(),
    navController: NavController,
    activityViewModel: ActivityViewModel,
    isInWelcomeScreen: Boolean = true,
    closeBottomSheet: () -> Unit
) {
    val hazeState = remember { HazeState() }
    NasimTheme {
        Box(Modifier.fillMaxSize()) {

            if (!isInWelcomeScreen) {
                AsyncImage(
                    modifier = modifier
                        .fillMaxSize()
                        .haze(hazeState),
                    model = activityViewModel.imageRequest,
                    contentScale = ContentScale.Crop,
                    contentDescription = ""
                )
            } else {
                AsyncImage(
                    modifier = modifier
                        .fillMaxSize()
                        .haze(hazeState),
                    model = ImageRequest.Builder(
                        LocalContext.current
                    )
                        .data("https://unsplash.com/photos/KsIw59jWXaI/download?ixid=M3wxMjA3fDB8MXxzZWFyY2h8Mjk3fHx3aW5kfGVufDB8MXx8fDE3MzQ5MDMwNjN8Mg&force=true&w=2400")
                        .build(),
                    contentDescription = "",
                    contentScale = ContentScale.Crop,
                    onError = {
                        println(it.result.throwable.message)
                    }
                )
            }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .hazeChild(hazeState, style = HazeStyle(noiseFactor = 0f)),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(
                    Modifier.height(
                        WindowInsets.statusBars.asPaddingValues().calculateTopPadding() + 32.dp
                    )
                )
                if (isInWelcomeScreen) {
                    Image(
                        painter = painterResource(R.drawable.nasim_outline),
                        modifier = modifier.size(150.dp),
                        contentDescription = "nasim logo"
                    )
                    Spacer(Modifier.height(32.dp))
                    Text(
                        "Welcome to The Nasim",
                        style = MaterialTheme.typography.headlineMedium,
                        color = Color.White,
                        fontFamily = fontFamily,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(Modifier.height(32.dp))
                }
                Text(
                    "Select Your Location :",
                    style = MaterialTheme.typography.titleLarge,
                    fontFamily = fontFamily,
                    color = Color.White,
                    modifier = modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center
                )
                Spacer(Modifier.height(24.dp))
                TextField(
                    value = viewModel.searchValue.value,
                    onValueChange = { viewModel.onEvent(WelcomeScreenEvents.OnSearchValueChanges(it)) },
                    trailingIcon = {
                        Icon(
                            painter = painterResource(R.drawable.search_lg),
                            tint = Color.White.copy(0.5f),
                            contentDescription = "search",
                            modifier = Modifier.padding(end = 5.dp, bottom = 3.dp)
                        )
                    },
                    placeholder = { Text("City Name") },
                    colors = TextFieldDefaults.colors(
                        unfocusedIndicatorColor = Color.Transparent,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent,
                        focusedContainerColor = Color.Transparent
                    ),
                    shape = RoundedCornerShape(15.dp),
                    modifier = Modifier
                        .fillMaxWidth(0.8f)
                        .hazeChild(hazeState)
                        .border(
                            2.dp,
                            Brush.verticalGradient(
                                listOf(
                                    MaterialTheme.colorScheme.primary.copy(0.2f),
                                    MaterialTheme.colorScheme.primary.copy(0.8f)
                                )
                            ), RoundedCornerShape(15.dp)
                        )
                        .background(
                            shape = RoundedCornerShape(15.dp), brush = Brush.verticalGradient(
                                startY = 0f, endY = 200f, colors =
                                    listOf(
                                        Color(0xFF414141).copy(0.9f),
                                        Color(0xFF212121).copy(0.9f)
                                    )
                            )
                        )
                )
                Spacer(Modifier.height(24.dp))

                AnimatedVisibility(
                    visible = viewModel.searchValue.value.isNotBlank(),
                    enter = fadeIn(),
                    exit = fadeOut()
                ) {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth(0.8f)
                            .heightIn(min = 100.dp)
                            .border(
                                1.5.dp,
                                Color.White.copy(0.8f),
                                shape = RoundedCornerShape(20.dp)
                            )
                            .clip(RoundedCornerShape(20.dp))
                            .hazeChild(state = hazeState)
                            .background(
                                brush = Brush.verticalGradient(
                                    listOf(
                                        Color(0xff2E2E2E).copy(0.9f),
                                        Color(0xff949494).copy(0.2f)
                                    )
                                )
                            )
                    ) {
                        items(items = viewModel.coordinates, key = { it.lat }) { coordinateItem ->
                            CityItem(
                                viewModel = viewModel,
                                cityName = coordinateItem.name,
                                lat = coordinateItem.lat,
                                lon = coordinateItem.lon,
                                text = "${coordinateItem.name}, ${coordinateItem.region}, ${coordinateItem.country}"
                            ) {
                                if (coordinateItem.lat == viewModel.selectedItem.value.first)
                                    viewModel.onEvent(WelcomeScreenEvents.OnSelectItem(0.0, 0.0))
                                else
                                    viewModel.onEvent(
                                        WelcomeScreenEvents.OnSelectItem(
                                            coordinateItem.lat,
                                            coordinateItem.lon
                                        )
                                    )


                            }
                        }

                    }
                }

                Spacer(Modifier.height(32.dp))
                val coroutineScope = rememberCoroutineScope()
                AnimatedVisibility(viewModel.selectedItem.value.first != 0.0 && viewModel.selectedItem.value.second != 0.0) {
                    Button(
                        onClick = {
                            coroutineScope.launch {
                                viewModel.onEvent(WelcomeScreenEvents.SaveData)
                                if (!isInWelcomeScreen) {
                                    activityViewModel.getWeather(
                                        viewModel.selectedItem.value.first,
                                        viewModel.selectedItem.value.second
                                    )
                                    closeBottomSheet()
                                }
                            }




                            navController.popBackStack()
                            navController.navigate(DestinationRoutes.HOME_SCREEN.route)
                        },
                        shape = RoundedCornerShape(20.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.Transparent,
                            contentColor = Color.White
                        ),
                        modifier = Modifier
                            .fillMaxWidth(0.8f)
                            .background(
                                shape = RoundedCornerShape(20.dp),
                                brush = Brush.verticalGradient(
                                    startY = -80f,
                                    endY = 200f,
                                    colors = listOf(Color(0xff83FF8C), Color(0xff000000))
                                )
                            )
                            .border(
                                width = 1.5.dp, brush = Brush.verticalGradient(
                                    listOf(Color.White.copy(0.5f), Color.White.copy(0.3f))
                                ),
                                shape = RoundedCornerShape(20.dp)
                            )
                    ) {
                        Text(
                            "Let's Get Started !",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(5.dp)
                        )
                    }
                }


            }

        }
    }


}

@Composable
fun CityItem(
    modifier: Modifier = Modifier,
    viewModel: WelcomeScreenViewModel,
    text: String,
    cityName: String,
    lat: Double,
    lon: Double,
    onClick: () -> Unit
) {

    val animateColorByState by animateColorAsState(
        if (viewModel.selectedItem.value.first == lat && viewModel.selectedItem.value.second == lon) Color(
            0xff83FF8C
        ).copy(0.4f) else Color.Transparent,
        label = "colorState"
    )
    Column(
        modifier
            .fillMaxWidth()
            .clickable {
                onClick()
            }
            .background(animateColorByState)) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = text,
                fontFamily = fontFamily,
                color = Color.White,
                fontWeight = FontWeight.Bold,
            )
            Spacer(Modifier.weight(1f))
            AnimatedVisibility(
                visible = viewModel.selectedItem.value.first == lat && viewModel.selectedItem.value.second == lon,
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                Icon(
                    painter = painterResource(R.drawable.check_circle_broken),
                    contentDescription = "next",
                    tint = Color.White
                )
            }

        }
        if (viewModel.coordinates.indexOf(viewModel.coordinates.find { it.name == cityName }) != viewModel.coordinates.lastIndex)
            HorizontalDivider(color = Color.White.copy(0.5f))
    }

}
