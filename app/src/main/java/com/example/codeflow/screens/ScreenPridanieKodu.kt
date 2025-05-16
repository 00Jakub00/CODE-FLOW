package com.example.codeflow.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.codeflow.ZdielanieKnizniceKodov
import kotlinx.coroutines.launch

@Composable
fun NapisKodScreen(
    navController: NavController,
    viewModel: ZdielanieKnizniceKodov
) {
    var kodText by remember { mutableStateOf("") }
    var showSaveDialog by remember { mutableStateOf(false) }
    var nazovKodu by remember { mutableStateOf("") }
    val scope = rememberCoroutineScope()

    if (showSaveDialog) {
        AlertDialog(
            onDismissRequest = { showSaveDialog = false },
            title = { Text("Zadaj názov kódu") },
            text = {
                OutlinedTextField(
                    value = nazovKodu,
                    onValueChange = { nazovKodu = it },
                    label = { Text("Názov") },
                    singleLine = true
                )
            },
            confirmButton = {
                TextButton(onClick = {
                    if (nazovKodu.isNotBlank() && kodText.isNotBlank()) {
                        scope.launch {
                            viewModel.kniznicaKodov.pridajKod(kodText.trim(), nazovKodu.trim())
                            showSaveDialog = false
                            nazovKodu = ""
                            kodText = ""
                        }
                    }
                }) {
                    Text("OK")
                }
            },
            dismissButton = {
                TextButton(onClick = {
                    showSaveDialog = false
                    nazovKodu = ""
                }) {
                    Text("Zrušiť")
                }
            }
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("Napíš svoj kód", style = MaterialTheme.typography.titleLarge)

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.75f)
        ) {
            val scrollState = rememberScrollState()
            OutlinedTextField(
                value = kodText,
                onValueChange = { kodText = it },
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(scrollState),
                placeholder = { Text("Sem napíš svoj kód...") },
                maxLines = Int.MAX_VALUE,
                singleLine = false,
                colors = TextFieldDefaults.colors(
                    focusedIndicatorColor = Color(77, 163, 191),
                    unfocusedIndicatorColor = Color(77, 163, 191),
                ),

            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Button(onClick = { navController.popBackStack() },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(77, 163, 191),
                    contentColor = Color.White
                ),
                shape = RoundedCornerShape(13.dp),) {
                Text("Späť")
            }

            Button(onClick = { showSaveDialog = true },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(38, 82, 97),
                    contentColor = Color.White
                ),) {
                Text("Uložiť")
            }

            Button(onClick = { kodText = "" }, colors = ButtonDefaults.buttonColors(
                containerColor = Color(38, 82, 97),
                contentColor = Color.White
            ),) {
                Text("Vymazať")
            }
        }
    }
}

