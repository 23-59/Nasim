package com.golden_minute.nasim.presentation.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.ui.graphics.Color
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
                HomePage()
            }
        }
    }
}






