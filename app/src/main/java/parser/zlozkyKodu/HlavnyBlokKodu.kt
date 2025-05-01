package com.example.visualizationofcode.ui.theme.zloky.kodu

import org.example.zlozkyKodu.InformativnyPrikaz


class HlavnyBlokKodu(kod: String) : KodovyBlok(getCodeStatements(kod)) {

    companion object {
           private fun getCodeStatements(kod: String): MutableList<String> {
               val regex = "(\\{\\}|\\}|\\{|;|[^{};]+)".toRegex()
                    val rozdelenyKod = regex.findAll(kod).map { it.value.trim() }.filter { it.isNotEmpty() }.toList()
        //            println("Pocet prikazov: ${rozdelenyKod.size}")
         //           rozdelenyKod.forEach { println(it) }
                    return rozdelenyKod.toMutableList()
       }
    }
    fun buttonExecute() {
        hotovePrikazy.add(InformativnyPrikaz("Začiatok programu!"))
        spracujPrikazy()
        hotovePrikazy.add(InformativnyPrikaz("Koniec programu!"))
        vypisVyhodnotenePrikazy()
    }

}