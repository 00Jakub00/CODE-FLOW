package com.example.visualizationofcode.ui.theme.zloky.kodu

import org.example.zlozkyKodu.InformativnyPrikaz


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

    fun zaciatokProgramu(): String {
        return hotovePrikazyVPoradi[prikazCislo].vyhodnotKod()
    }

    fun spracujKod() {
        hotovePrikazy.add(InformativnyPrikaz("Začiatok programu!"))
        spracujPrikazy()
        hotovePrikazy.add(InformativnyPrikaz("Koniec programu!"))

        hotovePrikazyVPoradi = dajMiTvojePrikazy()
    }

     fun dajMiNasledujuciVyhodnotenyPrikaz(): String {
         if (prikazCislo < hotovePrikazyVPoradi.size - 1) {
             prikazCislo++
         }
         return hotovePrikazyVPoradi[prikazCislo].vyhodnotKod()
     }

    fun dajMiPredchadzajuciVyhodnotenyPrikaz(): String {
        if (prikazCislo > 0) {
            prikazCislo--
        }
        return hotovePrikazyVPoradi[prikazCislo].vyhodnotKod()
    }

}