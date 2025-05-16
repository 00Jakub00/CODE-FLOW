package com.example.codeflow

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.compose.ui.text.AnnotatedString
import com.example.visualizationofcode.ui.theme.zloky.kodu.CastKodu
import com.example.visualizationofcode.ui.theme.zloky.kodu.HlavnyBlokKodu
import org.example.zlozkyKodu.CyklickyBlok
import parser.zlozkyKodu.PrikazVystupu

class KrokovanieKodu(var textKodu: String) : ViewModel() {
    val historiaZvyrazneni: MutableList<Zvyraznenie> = mutableListOf()
    val deterministika: Deterministika = Deterministika()
    var zvyraznenie by mutableStateOf(Zvyraznenie())
    val parser: HlavnyBlokKodu = HlavnyBlokKodu(textKodu)
    var cisloRiadku: Int = -1
    var vystupnyText by mutableStateOf("")
    var vstupVystupCyklus = 0;

    init  {
        historiaZvyrazneni.add(zvyraznenie.nakopirujSa())
        parser.spracujKod()
        vystupnyText = parser.zaciatokProgramu()
        vytvorZvyrazneniaPreJednotliveKroky()
    }

    fun vytvorZvyrazneniaPreJednotliveKroky() {
        while (!parser.bolPrikazPosledny()) {
            parser.prikazCislo++
            cisloRiadku++
            cisloRiadku = deterministika.operaciaVytvaraniaZvyrazneni(
                parser,
                zvyraznenie,
                cisloRiadku,
                textKodu
            )
            historiaZvyrazneni.add(zvyraznenie.nakopirujSa())
        }
        parser.prikazCislo = 0
    }

    fun dajMiAktualnyPrikaz(): Int {
        return parser.prikazCislo
    }

    fun dajMiPocetPrikazov(): Int {
        return parser.hotovePrikazyVPoradi.size - 1
    }

    fun klikVpred() {
        if (parser.hotovePrikazyVPoradi.size - 1 > parser.prikazCislo) {
            vystupnyText = parser.dajMiNasledujuciVyhodnotenyPrikaz()

            if (parser.jeAtualnyPrikazCyklus()) {
                vstupVystupCyklus++
            } else if (parser.jeAkualnyPrikazVystupny()) {
                var p = parser.dajMiAktualnyPrikaz() as PrikazVystupu
                if (p.typBlokuZKtorehoSaVystupuje == "cyklus") {
                    vstupVystupCyklus--
                }
            }

            zvyraznenie = historiaZvyrazneni.get(parser.prikazCislo)
        }
    }

    fun klikVzad() {
        if (parser.prikazCislo > 0) {
            vystupnyText = parser.dajMiPredchadzajuciVyhodnotenyPrikaz()

            if (parser.dajMiDalsiPrikaz() is CyklickyBlok) {
                vstupVystupCyklus--
            } else if ((parser.dajMiDalsiPrikaz() is PrikazVystupu)) {
                var p = parser.dajMiDalsiPrikaz() as PrikazVystupu
                if (p.typBlokuZKtorehoSaVystupuje == "cyklus") {
                    vstupVystupCyklus++
                }
            }

            zvyraznenie = historiaZvyrazneni.get(parser.prikazCislo)
        }
    }

    fun skokNaKrok(krok: Int) {
        if (krok >= 0 && krok <= parser.hotovePrikazyVPoradi.size - 1) {
            vstupVystupCyklus = 0
            parser.prikazCislo = 0
            while (parser.prikazCislo < krok) {
                var p = parser.dajMiAktualnyPrikaz()
                if (p is CyklickyBlok) {
                    vstupVystupCyklus++
                } else if (p is PrikazVystupu) {
                    if (p.typBlokuZKtorehoSaVystupuje == "cyklus") {
                        vstupVystupCyklus--
                    }
                }
                parser.prikazCislo++
            }

            var p = parser.dajMiAktualnyPrikaz()
            if (p is CyklickyBlok) {
                vstupVystupCyklus++
            } else if (p is PrikazVystupu) {
                if (p.typBlokuZKtorehoSaVystupuje == "cyklus") {
                    vstupVystupCyklus--
                }
            }

            vystupnyText = parser.dajMiAktualnyVyhodnotenyPrikaz()
            zvyraznenie = historiaZvyrazneni.get(parser.prikazCislo)
        } else {
            zvyraznenie = historiaZvyrazneni.get(0)
            vystupnyText = "Príkaz s daným číslom tu nie je!"
        }
    }

    fun skip() {
        if (vstupVystupCyklus > 0) {
            var p: CastKodu? = null

            do {
                parser.prikazCislo++
                p = parser.dajMiAktualnyPrikaz()
            } while(!(p is PrikazVystupu) || !(p.typBlokuZKtorehoSaVystupuje == "cyklus"))

            vystupnyText = parser.dajMiAktualnyVyhodnotenyPrikaz()
            zvyraznenie = historiaZvyrazneni.get(parser.prikazCislo)
            vstupVystupCyklus--
        } else {
            klikVpred()
        }
    }

    fun dajMiZvyrazneneRiadky(): AnnotatedString {
        return zvyraznenie.zvyrazniRiadky(textKodu)
    }
}
