package org.example.zlozkyKodu

import com.example.visualizationofcode.ui.theme.zloky.kodu.CastKodu
import com.example.visualizationofcode.ui.theme.zloky.kodu.KodovyBlok
import com.example.visualizationofcode.ui.theme.zloky.kodu.TypKodovehoBloku
import com.example.visualizationofcode.ui.theme.zloky.kodu.Vyraz

class Vypis(val kod: String, val akyVypis: TypKodovehoBloku): CastKodu {
    companion object {
         fun rozborPrikazu(kodovyBlok: KodovyBlok, prikaz: String): Boolean {
            if (kodovyBlok.zistiOAkyTypKoduSaJedna(prikaz) == TypKodovehoBloku.VYPIS_NOVY_RIADOK) {
                kodovyBlok.hotovePrikazy.add(spracovaniePrikazuVypis(prikaz, TypKodovehoBloku.VYPIS_NOVY_RIADOK, kodovyBlok.premenne))
            } else {
                kodovyBlok.hotovePrikazy.add(spracovaniePrikazuVypis(prikaz, TypKodovehoBloku.VYPIS, kodovyBlok.premenne))
            }
            return false
        }

        fun spracovaniePrikazuVypis(
            kod: String,
            typVypisu: TypKodovehoBloku,
            nadradenePremenne: MutableList<Array<Any?>?>
        ): Vypis {
            var vyraz = Vyraz.nabalVyraz(kod, true)

            val vyhodnotenyKod = Vyraz.vyhodnotVyraz(vyraz, nadradenePremenne)

         //   println("Pridavam vypis tento: $vyhodnotenyKod")
            
            return if (typVypisu == TypKodovehoBloku.VYPIS_NOVY_RIADOK) Vypis(
                vyhodnotenyKod.toString(),
                TypKodovehoBloku.VYPIS_NOVY_RIADOK
            ) else
                Vypis(vyhodnotenyKod.toString(), TypKodovehoBloku.VYPIS)
        }

    }

    override fun vyhodnotKod(): String {
        return if (akyVypis == TypKodovehoBloku.VYPIS) "Tento kód sa vypíše na terminál \"$kod\" a" +
                " riadok ostáva" else "Tento kód sa vypíše na terminál \"$kod\" a" +
                " prejde sa na nový riadok"
    }
}