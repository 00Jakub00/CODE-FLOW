package com.example.codeflow.screens

import android.app.Application
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.codeflow.KrokovanieKodu
import com.example.codeflow.ZdielanieKnizniceKodov


@Composable
fun ScreenKrokovanie(
    modifier: Modifier = Modifier,
    navController: NavController,
    viewModel: ZdielanieKnizniceKodov
) {
    val aktualnyKod = viewModel.kniznicaKodov.aktualneVybranyKod

    val krokovanieKodu: KrokovanieKodu = viewModel(
        factory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
                if (modelClass.isAssignableFrom(KrokovanieKodu::class.java)) {
                    return KrokovanieKodu(aktualnyKod!!) as T
                }
                throw IllegalArgumentException("Unknown ViewModel class")
            }
        }
    )

    val scrollState = rememberScrollState()

    var showDialog by remember { mutableStateOf(false) }
    var inputText by remember { mutableStateOf("") }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text("Zadaj číslo kroku") },
            text = {
                TextField(
                    value = inputText,
                    onValueChange = { inputText = it },
                    placeholder = { Text("") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )
            },
            confirmButton = {
                Button(onClick = {
                    val krok = inputText.toIntOrNull()
                    if (krok != null) {
                        krokovanieKodu.skokNaKrok(krok)
                    }
                    showDialog = false
                    inputText = ""
                }, colors = ButtonDefaults.buttonColors(
                    containerColor = Color(77, 163, 191),
                    contentColor = Color.White
                ),) {
                    Text("OK")
                }
            },
            dismissButton = {
                Button(onClick = {
                    showDialog = false
                    inputText = ""
                },colors = ButtonDefaults.buttonColors(
                        containerColor = Color(77, 163, 191),
                    contentColor = Color.White
                ),) {
                    Text("Zrušiť")
                }
            }
        )
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "VIZUALIZÁCIA",
            modifier = Modifier.align(Alignment.CenterHorizontally),
            fontSize = 23.sp,
            style = LocalTextStyle.current.copy(fontWeight = FontWeight.Bold)
        )
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(LocalConfiguration.current.screenHeightDp.dp * 0.3f)
                .height(150.dp)
                .verticalScroll(scrollState)
                .border(1.dp, Color.Gray)
        ) {
            Text(
                text = krokovanieKodu.dajMiZvyrazneneRiadky(),
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White),
                style = LocalTextStyle.current.copy(lineHeight = 20.sp)
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {

            Button(onClick = {
                krokovanieKodu.klikVzad()
            },
                modifier = Modifier.weight(0.6f),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(77, 163, 191),
                    contentColor = Color(240, 191, 127)
                ),
                shape = RoundedCornerShape(13.dp)
            ) {
                Text(
                    "<<",
                    fontSize = 25.sp,
                    color = Color.White
                )
            }

            Column(
                modifier = Modifier.weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Button(onClick = {
                    showDialog = true
                },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(38, 82, 97),
                        contentColor = Color(38, 82, 97)
                    )
                ) {
                    Text(
                        "Skok na krok",
                        color = Color.White
                    )
                }

                Button(onClick = {
                    krokovanieKodu.skip()
                },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(38, 82, 97),
                        contentColor = Color(38, 82, 97)
                    )
                ) {
                    Text(
                        "Skip",
                        color = Color.White
                    )
                }
            }

            Button(onClick = {
                krokovanieKodu.klikVpred()
            },
                modifier = Modifier.weight(0.6f),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(77, 163, 191),
                    contentColor = Color.White
                ),
                shape = RoundedCornerShape(13.dp)
            ) {
                Text(
                    ">>",
                    fontSize = 25.sp,
                    color = Color.White
                )
            }

        }

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            "KROKOVANIE",
            modifier = Modifier.align(Alignment.CenterHorizontally),
            fontSize = 23.sp,
            style = LocalTextStyle.current.copy(fontWeight = FontWeight.Bold)
        )
        TextField(
            value = krokovanieKodu.vystupnyText,
            onValueChange = {},
            readOnly = true,
            modifier = Modifier
                .fillMaxWidth()
                .height(LocalConfiguration.current.screenHeightDp.dp * 0.2f)
        )

        Spacer(modifier = Modifier.height(10.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Button(onClick = { navController.popBackStack() },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(77, 163, 191),
                    contentColor = Color.White
                ),
                shape = RoundedCornerShape(13.dp)
            ) {
                Text(
                    "Späť",
                    color = Color.White
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            Text(
                text = "${krokovanieKodu.dajMiAktualnyPrikaz()}/${krokovanieKodu.dajMiPocetPrikazov()}",
                textAlign = TextAlign.End
            )
        }
    }
}