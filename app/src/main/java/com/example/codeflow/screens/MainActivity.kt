package com.example.codeflow.screens

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val navController = rememberNavController()
            NavHost(navController, startDestination = "uvod") {
                composable("krokovanie") { ScreenKrokovanie(navController = navController) }
                composable("pridanieKodu") { NapisKodScreen(navController = navController) }
                composable("ulozeneKody") { UlozenyKodScreen(navController = navController) }
                composable("uvod") { UvodnyScreen(navController = navController) }
            }
        }
    }
}



