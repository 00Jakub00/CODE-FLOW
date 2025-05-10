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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
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

/*fun zvyrazneRiadky(text: String, lineIndices: List<Int>, highlightColor: Color): AnnotatedString {
    val lines = text.lines()
    return buildAnnotatedString {
        lines.forEachIndexed { index, line ->
            if (index in lineIndices) {
                withStyle(style = SpanStyle(color = highlightColor)) {
                    append(line)
                }
            } else {
                append(line)
            }
            if (index != lines.lastIndex) append("\n")
        }
    }
}*/


@Composable
fun CodeFlowScreen(modifier: Modifier = Modifier) {
    var textKodu by remember { mutableStateOf(
        """
        String q = "ahoj"; 
        int a = 5 * 6 - (12 / 4); 
        int b = 5 + 5; 
        a = 5 * b; 
        b = 19;
        for (int i = 0; i < 10; i++) {
            a = a + 8;
            b = a;
            System.out.println(a + " ahojjj");
        }
        """.trimIndent()) }
    val pocetRiadkov = textKodu.lines().size
    val deterministika = remember { Deterministika() }
    var cisloRiadku by remember { mutableStateOf(-1) }
    val zvyraznenie = remember { Zvyraznenie() }
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
                .border(1.dp, Color.Gray) 
        ) {
            Text(
                text = zvyraznenie.zvyrazniRiadky(textKodu, deterministika.odzvyraznenieRiadku),
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
                try {
                    val vystupnyText = parser?.dajMiNasledujuciVyhodnotenyPrikaz()
                        ?: "Parser nie je inicializovaný"
                    vystup = vystupnyText

                    cisloRiadku++
                    cisloRiadku = deterministika.operaciaKliknutieDoPredu(parser, zvyraznenie, cisloRiadku, textKodu)
                    zvyraznenie.pridajRiadok(cisloRiadku)

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

                    cisloRiadku = zvyraznenie.odoberADajMiPoslednyIndexRiadku()
                    deterministika.operaciaKliknutieDoZadu(parser, zvyraznenie)

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


