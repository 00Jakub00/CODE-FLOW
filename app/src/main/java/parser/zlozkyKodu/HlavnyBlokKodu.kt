package com.example.visualizationofcode.ui.theme.zloky.kodu

import org.example.zlozkyKodu.Break
import org.example.zlozkyKodu.Continue
import org.example.zlozkyKodu.CyklickyBlok
import org.example.zlozkyKodu.InformativnyPrikaz
import parser.zlozkyKodu.PrikazVystupu


class HlavnyBlokKodu(kod: String) : KodovyBlok(getCodeStatements(kod)) {
    var hotovePrikazyVPoradi: MutableList<CastKodu> = mutableListOf()
    var prikazCislo = 0

    companion object {
           private fun getCodeStatements(kod: String): MutableList<String> {
               val regex = "(\\{\\}|\\}|\\{|;|[^{};]+)".toRegex()
                    val rozdelenyKod = regex.findAll(kod).map { it.value.trim() }.filter { it.isNotEmpty() }.toList()
                    return rozdelenyKod.toMutableList()
       }
    }

    fun dajMiDalsiPrikaz(): CastKodu {
        return hotovePrikazyVPoradi[prikazCislo + 1]
    }

    fun dajMiAktualnyPrikaz(): CastKodu {
        return hotovePrikazyVPoradi[prikazCislo]
    }

    fun zaciatokProgramu(): String {
        return hotovePrikazyVPoradi[prikazCislo].vyhodnotKod()
    }


    fun bolPrikazPosledny(): Boolean {
        return prikazCislo == hotovePrikazyVPoradi.size - 1
    }

    fun spracujKod() {
        hotovePrikazy.add(InformativnyPrikaz("Začiatok programu!"))
        spracujPrikazy()
        hotovePrikazy.add(InformativnyPrikaz("Koniec programu!"))

        hotovePrikazyVPoradi = dajMiTvojePrikazy()
    }

    fun jeAkualnyPrikazVystupny(): Boolean {
        return hotovePrikazyVPoradi[prikazCislo] is PrikazVystupu
    }

    fun jeAktualnyPrikazIF(): Boolean {
        return hotovePrikazyVPoradi[prikazCislo] is IF
    }

    fun jeAktualnyPrikazInformativny(): Boolean {
        return hotovePrikazyVPoradi[prikazCislo] is InformativnyPrikaz
    }

    fun jeAtualnyPrikazCyklus(): Boolean {
        return hotovePrikazyVPoradi[prikazCislo] is CyklickyBlok
    }

    fun jeAktualnyPrikazContinue(): Boolean {
        return hotovePrikazyVPoradi[prikazCislo] is Continue
    }

    fun jeAktualnyPrikazBreak(): Boolean {
        return hotovePrikazyVPoradi[prikazCislo] is Break
    }

     fun dajMiNasledujuciVyhodnotenyPrikaz(): String {
         if (prikazCislo < hotovePrikazyVPoradi.size - 1) {
             prikazCislo++
         }
         return hotovePrikazyVPoradi[prikazCislo].vyhodnotKod()
     }

    fun dajMiAktualnyVyhodnotenyPrikaz(): String {
        return hotovePrikazyVPoradi[prikazCislo].vyhodnotKod()
    }

    fun dajMiPredchadzajuciVyhodnotenyPrikaz(): String {
        if (prikazCislo > 0) {
            prikazCislo--
            return hotovePrikazyVPoradi[prikazCislo].vyhodnotKod()
        } else {
            return hotovePrikazyVPoradi[0].vyhodnotKod()
        }

    }

}
