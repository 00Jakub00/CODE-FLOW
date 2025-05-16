package com.example.codeflow.screens

import android.app.Application
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.codeflow.R
import com.example.codeflow.ZdielanieKnizniceKodov

@Composable
fun UvodnyScreen(
    navController: NavController,
    viewModel: ZdielanieKnizniceKodov
) {
    var showInfoDialog by remember { mutableStateOf(false) }

    if (showInfoDialog) {
        AlertDialog(
            onDismissRequest = {
                showInfoDialog = false
            },
            title = {
                Text("Upozornenie")
            },
            text = {
                Text("Najskôr musíte vybrať kód.")
            },
            confirmButton = {
                TextButton(onClick = {
                    showInfoDialog = false
                }) {
                    Text("OK")
                }
            }
        )
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "CODE  FLOW",
                fontSize = 36.sp,
                fontWeight = FontWeight.Bold,
                color = Color(59, 208, 187)
            )

            Spacer(modifier = Modifier.height(8.dp))

            Image(
                painter = painterResource(id = R.drawable.code_flow_logo),
                contentDescription = "Logo Code Flow",
                modifier = Modifier
                    .height(120.dp)
                    .fillMaxWidth(),
                contentScale = ContentScale.Fit
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "\"Každý riadok má svoj zmysel. Code Flow ti ho ukáže.\"",
                fontSize = 16.sp,
                fontWeight = FontWeight.Light,
                color = Color.Gray,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(40.dp))

            Button(
                onClick = {
                    if (viewModel.kniznicaKodov.jeVybranyKod()) {
                        navController.navigate("krokovanie")
                    } else {
                        showInfoDialog = true
                    }
                          },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(38, 82, 97),
                    contentColor = Color.White
                )
            ) {
                Text("Krokovanie kódu")
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = { navController.navigate("ulozeneKody") },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(38, 82, 97),
                    contentColor = Color.White
                )
            ) {
                Text("Vybrať kód")
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = { navController.navigate("pridanieKodu") },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(38, 82, 97),
                    contentColor = Color.White
                )
            ) {
                Text("Napísať kód")
            }

            Spacer(modifier = Modifier.height(48.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Button(onClick = {
                    Log.d("Zmena", "Vybrany kod je: ${viewModel.kniznicaKodov.aktualneVybranyKod}")
                    viewModel.kniznicaKodov.zrusitVyber()
                    Log.d("Zmena", "Vybrany kod je: ${viewModel.kniznicaKodov.aktualneVybranyKod}")},
                    modifier = Modifier.wrapContentWidth(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(77, 163, 191),
                        contentColor = Color.White
                    ),
                    shape = RoundedCornerShape(13.dp)
                ) {
                    Text(
                        "Zrušiť výber",
                        color = Color.White
                    )
                }
            }
        }
    }
}
