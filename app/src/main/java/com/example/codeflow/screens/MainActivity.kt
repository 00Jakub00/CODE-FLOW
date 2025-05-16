package com.example.codeflow.screens

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.compose.ui.platform.LocalContext
import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.codeflow.KniznicaKodov
import com.example.codeflow.ZdielanieKnizniceKodov


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val application = LocalContext.current.applicationContext as Application
            val viewModel: ZdielanieKnizniceKodov = viewModel(
                factory = ZdielanieKnizniceKodovFactory(application)
            )


            val navController = rememberNavController()
            NavHost(navController, startDestination = "uvod") {
                composable("krokovanie") { ScreenKrokovanie(navController = navController, viewModel = viewModel) }
                composable("pridanieKodu") { NapisKodScreen(navController = navController, viewModel = viewModel) }
                composable ("ulozeneKody") { UlozenyKodScreen(navController = navController, viewModel = viewModel) }
                composable("uvod") { UvodnyScreen(navController = navController, viewModel = viewModel) }
            }
        }
    }
}



