package com.golden_minute.nasim.presentation.main

import android.annotation.SuppressLint
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.golden_minute.nasim.presentation.onboarding.WelcomeScreen
import com.golden_minute.nasim.presentation.onboarding.WelcomeScreenViewModel
import org.koin.androidx.compose.koinViewModel

@SuppressLint("SuspiciousIndentation")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChangeLocation(
    modifier: Modifier = Modifier,
    welcomeScreenViewModel: WelcomeScreenViewModel = koinViewModel(),
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