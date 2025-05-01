package org.example.zlozkyKodu

import com.example.visualizationofcode.ui.theme.zloky.kodu.CastKodu
import com.example.visualizationofcode.ui.theme.zloky.kodu.KodovyBlok
import com.example.visualizationofcode.ui.theme.zloky.kodu.TypKodovehoBloku

open class CyklickyBlok : KodovyBlok() {

    var iteracia: Int = 0
    var brk: Boolean = false

    fun zistiMiKolKoPrikazovMamVsebe(prvaZatvorka: Boolean): Int  {
        var prikaz = ""
        val zatvorky: Array<Int> = arrayOf(0, 0)
        var prikazCislo = 0
        if (prvaZatvorka) {
            zatvorky[0]++
        }
        do {
            prikaz = zoznamPrikazov.get(prikazCislo)
          //  println("Toto mam v sebe: ${prikaz}")
            if (prikaz == "{") {
                zatvorky[0]++
            } else if (prikaz == "}") {
                zatvorky[1]++
            }
            prikazCislo++
        } while (zatvorky[0] != zatvorky[1] || zatvorky[0] == 0)

        return prikazCislo - 1
    }


}