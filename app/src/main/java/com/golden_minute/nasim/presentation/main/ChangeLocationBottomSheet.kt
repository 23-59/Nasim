package com.golden_minute.nasim.presentation.main

import android.annotation.SuppressLint
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.golden_minute.nasim.presentation.onboarding.WelcomeScreen
import com.golden_minute.nasim.presentation.onboarding.WelcomeScreenViewModel

@SuppressLint("SuspiciousIndentation")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChangeLocation(
    modifier: Modifier = Modifier,
    welcomeScreenViewModel: WelcomeScreenViewModel = hiltViewModel(),
    activityViewModel: ActivityViewModel,
    navController: NavController,
    onDismissRequest: () -> Unit
) {

    val bottomSheetState = rememberModalBottomSheetState(true)


        ModalBottomSheet(sheetState = bottomSheetState, dragHandle = null, onDismissRequest = { onDismissRequest() },) {
            WelcomeScreen(
                modifier = Modifier,
                viewModel = welcomeScreenViewModel,
                navController = navController,
                isInWelcomeScreen = false,
                activityViewModel = activityViewModel
            ){
                onDismissRequest()
            }
        }

}