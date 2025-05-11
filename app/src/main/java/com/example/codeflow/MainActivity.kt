package com.example.codeflow

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
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
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.codeflow.ui.theme.CodeFlowTheme
import androidx.lifecycle.viewmodel.compose.viewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CodeFlowTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    CodeFlowScreen(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}


@Composable
fun CodeFlowScreen(modifier: Modifier = Modifier) {
    val krokovanieKodu: KrokovanieKodu = viewModel()
    val scrollState = rememberScrollState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text("Zadaj Java kód:")
        Box(
            modifier = Modifier
                .fillMaxWidth()
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
            horizontalArrangement = Arrangement.spacedBy(8.dp) 
        ) {
            Button(onClick = {
                krokovanieKodu.klikVpred()
            }) {
                Text("Ďalší krok")
            }

            Button(onClick = {
                krokovanieKodu.klikVzad()
            }) {
                Text("Krok späť")
            }
        }
        Spacer(modifier = Modifier.height(16.dp))

        Text("Výstup:")
        TextField(
            value = krokovanieKodu.vystupnyText,
            onValueChange = {},
            readOnly = true,
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
        )
    }
}


