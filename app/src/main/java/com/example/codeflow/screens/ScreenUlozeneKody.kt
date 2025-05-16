package com.example.codeflow.screens

import android.app.Application
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.codeflow.ZdielanieKnizniceKodov
import kotlinx.coroutines.launch

@Composable
fun UlozenyKodScreen(
    navController: NavController,
    viewModel: ZdielanieKnizniceKodov
) {
    val scope = rememberCoroutineScope()
    var showDialog by remember { mutableStateOf(false) }
    var kody by remember { mutableStateOf<List<String>>(emptyList()) }
    var vybranyIndex by remember { mutableStateOf<Int?>(null) }
    var kodNaOdstranenie by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(Unit) {
        kody = viewModel.kniznicaKodov.ziskajVsetkyKody()
    }

    if (showDialog && kodNaOdstranenie != null) {
        AlertDialog(
            onDismissRequest = {
                showDialog = false
            },
            title = {
                Text("Potvrdenie odstránenia")
            },
            text = {
                Text("Naozaj chcete odstrániť tento kód?")
            },
            confirmButton = {
                TextButton(onClick = {
                    scope.launch {
                        viewModel.kniznicaKodov.odstranKod(kodNaOdstranenie!!)
                        showDialog = false
                        kody = kody.filter { it != kodNaOdstranenie }
                        kodNaOdstranenie = null
                    }
                }) {
                    Text("Áno")
                }
            },
            dismissButton = {
                TextButton(onClick = {
                    showDialog = false
                    kodNaOdstranenie = null
                }) {
                    Text("Nie")
                }
            }
        )
    }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text(text = "Uložené kódy", style = MaterialTheme.typography.headlineMedium)

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .height(LocalConfiguration.current.screenHeightDp.dp * 0.75f)  // fixná výška
        ) {
            itemsIndexed(kody) { index, kodNazov ->
                val isSelected = vybranyIndex == index
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { vybranyIndex = index }
                        .background(if (isSelected) Color.LightGray else Color.Transparent)
                        .padding(12.dp)
                ) {
                    Text(text = kodNazov, style = MaterialTheme.typography.bodyLarge)
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {

            Button(onClick = { navController.popBackStack() },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(77, 163, 191),
                    contentColor = Color.White
                ),
                shape = RoundedCornerShape(13.dp)
            ) {
                Text("Späť")
            }

            Button(
                onClick = {
                    vybranyIndex?.let { index ->
                        val vybranyKod = kody[index]
                        scope.launch {
                            viewModel.kniznicaKodov.nastavVybranyKod(vybranyKod)
                        }

                    }
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(38, 82, 97),
                    contentColor = Color.White
                ),
                shape = RoundedCornerShape(13.dp),
                enabled = vybranyIndex != null
            ) {
                Text("Vybrať")
            }

            Button(
                onClick = {
                    vybranyIndex?.let { index ->
                        kodNaOdstranenie = kody[index]
                        showDialog = true
                    }
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(38, 82, 97),
                    contentColor = Color.White
                ),
                shape = RoundedCornerShape(13.dp),
                enabled = vybranyIndex != null
            ) {
                Text("Odstrániť kód")
            }

        }
    }
}