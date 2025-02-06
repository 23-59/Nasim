package com.golden_minute.nasim.presentation.search

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.withFrameMillis
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.golden_minute.nasim.R
import com.golden_minute.nasim.presentation.main.ActivityViewModel
import com.golden_minute.nasim.presentation.main.MainWeatherInfoSection
import dev.chrisbanes.haze.HazeState
import dev.chrisbanes.haze.haze
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
fun SearchScreen(
    modifier: Modifier = Modifier,
    searchScreenViewModel: SearchScreenViewModel,
    activityViewModel: ActivityViewModel
) {
    var enteredText by remember{ mutableStateOf(searchScreenViewModel.searchValue.value) }
    var debouncedText by remember{ mutableStateOf(searchScreenViewModel.searchValue.value) }
    val coroutineScope = rememberCoroutineScope()
    var debounceJob: Job? = null




    LaunchedEffect(enteredText) {
        debounceJob?.cancel()

        debounceJob = coroutineScope.launch {
            delay(2000)
            withContext(Dispatchers.Main) {
                if(debouncedText!= enteredText){
                    debouncedText = enteredText
                    searchScreenViewModel.onEvent(
                        SearchScreenEvents.OnSearchValueChanges(
                            debouncedText
                        )
                    )
                }

            }

        }
    }


    Box(Modifier
        .fillMaxSize()
        .haze(activityViewModel.hazeStateForNavigationBar)) {
        AsyncImage(
            model = activityViewModel.imageRequest,
            contentDescription = "",
            contentScale = ContentScale.FillBounds,
            modifier = Modifier.fillMaxSize().haze(activityViewModel.hazeState)

        )

        Column(
            modifier
                .fillMaxSize()
        ) {
            val focusRequester = remember { FocusRequester() }



            TextField(
                value = enteredText,
                onValueChange = {
                    enteredText = it
                },
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
                    .fillMaxWidth()
                    .padding(
                        top = WindowInsets.statusBars
                            .asPaddingValues()
                            .calculateTopPadding() + 12.dp, start = 24.dp, end = 24.dp
                    ).focusRequester(focusRequester)
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

            LaunchedEffect(Unit) {
                if (searchScreenViewModel.searchValue.value.isBlank())
                focusRequester.requestFocus()
            }

            LazyColumn(modifier = Modifier
                .fillMaxHeight()
                , contentPadding = PaddingValues(bottom = 130.dp, top = 32.dp)
            ) {
                items(searchScreenViewModel.weatherListState) { weatherItem ->

                    MainWeatherInfoSection(
                        searchScreenViewModel.showLoadingState.value,
                        weatherItem.current?.condition!!.code,
                        weatherItem.current.isDay,
                        weatherItem.current.condition.text,
                        weatherItem.location!!.name,
                        weatherItem.current.tempC.toString(),
                        weatherItem.current.feelslikeC.toString(),
                        activityViewModel.hazeState,
                        modifier = Modifier.padding(horizontal = 8.dp)
                    )
                    Spacer(Modifier.height(12.dp))
                }
            }

        }
        Box(
            Modifier
                .fillMaxWidth()
                .height(WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding())
                .background(MaterialTheme.colorScheme.surface)
                .align(Alignment.BottomCenter)
        )
    }

}



