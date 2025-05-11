package com.example.codeflow

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.compose.ui.text.AnnotatedString
import com.example.visualizationofcode.ui.theme.zloky.kodu.HlavnyBlokKodu

class KrokovanieKodu : ViewModel() {
    val historiaZvyrazneni: MutableList<Zvyraznenie> = mutableListOf()
    val deterministika: Deterministika = Deterministika()
    var zvyraznenie by mutableStateOf(Zvyraznenie())
    val kniznicaKodov: KniznicaKodov = KniznicaKodov()
    var textKodu: String = kniznicaKodov.kody.get(0)
    val parser: HlavnyBlokKodu = HlavnyBlokKodu(textKodu)
    var cisloRiadku: Int = -1
    var vystupnyText by mutableStateOf("")

    init {
        historiaZvyrazneni.add(zvyraznenie.nakopirujSa())
        Log.d("KrokovanieKodu", "Zvyraznenie: ${historiaZvyrazneni.size}")
        parser.spracujKod()
        vystupnyText = parser.zaciatokProgramu()
        vytvorZvyrazneniaPreJednotliveKroky()
    }

    fun vytvorZvyrazneniaPreJednotliveKroky() {
        while (!parser.bolPrikazPosledny()) {
            parser.prikazCislo++
            cisloRiadku++
            cisloRiadku = deterministika.operaciaKliknutieDoPredu(
                parser,
                zvyraznenie,
                cisloRiadku,
                textKodu
            )
            historiaZvyrazneni.add(zvyraznenie.nakopirujSa())
        }
        parser.prikazCislo = 0
    }

    fun klikVpred() {
        if (parser.hotovePrikazyVPoradi.size - 1 > parser.prikazCislo) {
            vystupnyText = parser.dajMiNasledujuciVyhodnotenyPrikaz()
            zvyraznenie = historiaZvyrazneni.get(parser.prikazCislo)
        }
    }

    fun klikVzad() {
        if (parser.prikazCislo > 0) {
            vystupnyText = parser.dajMiPredchadzajuciVyhodnotenyPrikaz()
            zvyraznenie = historiaZvyrazneni.get(parser.prikazCislo)
        }
    }

    fun dajMiZvyrazneneRiadky(): AnnotatedString {
        return zvyraznenie.zvyrazniRiadky(textKodu)
    }

    fun vypisIndexy(zvyraznenie: Zvyraznenie) {
        Log.d("KrokovanieKodu", "${zvyraznenie.aktualnyRiadok}")
        Log.d("KrokovanieKodu", "${zvyraznenie.odzvyraznitRiadok}")
        for (zz in zvyraznenie.zvyraznene) {
            Log.d("KrokovanieKodu", "Farba: ${zz.farba}")
            for (index in zz.indexy) {
                Log.d("KrokovanieKodu", String.format("Index: %d", index))
            }
            Log.d("KrokovanieKodu", "\n")
        }
        Log.d("KrokovanieKodu", "\n")
    }
}