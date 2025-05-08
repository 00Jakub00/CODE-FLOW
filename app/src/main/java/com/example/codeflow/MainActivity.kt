package com.example.codeflow

import android.os.Bundle
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
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.codeflow.ui.theme.CodeFlowTheme
import com.example.visualizationofcode.ui.theme.zloky.kodu.HlavnyBlokKodu

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
    var textKodu by remember { mutableStateOf(
        """
        String q = "ahoj"; 
        int a = 5 * 6 - (12 / 4); 
        int b = 5 + 5; 
        a = 5 * b; 
        b = 19;
        for (int i = 0; i < 10; i = a + 1) {
            for (int j = 0; j < 8; j = j + 1) {
                a = a + 3;
                if (j == 3) {
                    int kk = 5;
                    continue;
                }
                continue;
                int leto = 12;
            }
        }
        a = a + 5; 
        b = b * 10;
        double c = 12.7;
        c = c + b;
        int aa = 5;
        int bb = 20;
        while (aa < bb) {
            a = a + 2;
            if (4 < 10) { 
                aa = aa + 2;
                break;
            }
            aa = aa + 1;
            System.out.println("Jirka ma " +  a   + " rokov.");
        }
        if (5 > 5) {
            System.out.print("4 je mensie ako 5, taka je pravdaaaa ");
        }
        else if (5 > 72) {
            System.out.print("4 je mensie ako 5, taka je pravda ");
        }
        else {
            System.out.println("Vetva poslednej možnosti");
        }
        int vajce = 200;
        do {
            if (vajce == 150) {
                vajce = vajce - 5;
                continue;
            }
            if (vajce == 120) {
                vajce = vajce - 5;
                break;
            }
            vajce--;
        } while(vajce > 100);
        int dado = 17;
        do {
            vajce--;
        } while(vajce > 50);
    """.trimIndent()) }

    val scrollState = rememberScrollState()
    val parser = remember(textKodu) {
        HlavnyBlokKodu(textKodu).apply { spracujKod() }
    }
    var vystup by remember { mutableStateOf(parser.zaciatokProgramu()) }

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
                .border(1.dp, Color.Gray) // voliteľné pre rámik
        ) {
            TextField(
                value = textKodu,
                onValueChange = { textKodu = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = 150.dp)
                    .background(Color.White), // voliteľné
                textStyle = LocalTextStyle.current.copy(lineHeight = 20.sp)
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp) // medzera medzi tlačidlami
        ) {
            Button(onClick = {
                try {
                    val vystupnyText = parser?.dajMiNasledujuciVyhodnotenyPrikaz()
                        ?: "Parser nie je inicializovaný"
                    vystup = vystupnyText.toString()
                } catch (e: Exception) {
                    vystup = "Chyba: ${e.message}"
                }
            }) {
                Text("Ďalší krok")
            }

            Button(onClick = {
                try {
                    val vystupnyText = parser?.dajMiPredchadzajuciVyhodnotenyPrikaz()
                        ?: "Parser nie je inicializovaný"
                    vystup = vystupnyText.toString()
                } catch (e: Exception) {
                    vystup = "Chyba: ${e.message}"
                }
            }) {
                Text("Krok späť")
            }
        }
        Spacer(modifier = Modifier.height(16.dp))

        Text("Výstup:")
        TextField(
            value = vystup,
            onValueChange = {},
            readOnly = true,
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
        )
    }
}


