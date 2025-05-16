package com.example.visualizationofcode.ui.theme.zloky.kodu

import org.example.zlozkyKodu.CyklickyBlok
import org.example.zlozkyKodu.InformativnyPrikaz
import parser.zlozkyKodu.PrikazVystupu

class CyklusWhile(var podmienka: String): CyklickyBlok(), CastKodu {

    companion object  {
        fun spracovaniePrikazuWhile(prikaz: String): CyklusWhile {
            val regex = Regex("while\\s*\\((.*?)\\)")
            val match = regex.find(prikaz)
            val podmienka = match?.groups?.get(1)?.value
            return CyklusWhile(podmienka!!)
        }

         fun rozborPrikazu(kodovyBlok: KodovyBlok): Boolean {
            var cyklusWhile: CyklusWhile = spracovaniePrikazuWhile(kodovyBlok.zoznamPrikazov.get(kodovyBlok.poradieVykonanehoPrikazu))
            kodovyBlok.posunSaODalsiePrikazy(1)
            var novePrikazy: List<String> = kodovyBlok.zoznamPrikazov.slice(kodovyBlok.poradieVykonanehoPrikazu until kodovyBlok.zoznamPrikazov.size)

            cyklusWhile.pridajPrikazyAPremenne(novePrikazy, kodovyBlok.premenne)
            kodovyBlok.hotovePrikazy.add(cyklusWhile)
            return true
        }
    }

    override fun spracujPrikazy(): Int {
      //  zoznamPrikazov.forEach { println(it + " cykluWhile") }

        var pocetPrikazovVCykle = zistiMiKolKoPrikazovMamVsebe(false)

        while (Vyraz.vyhodnotVyraz(podmienka, premenne) as Boolean) {
            if (brk) {
                break
            }
            iteracia++
            hotovePrikazy.add(InformativnyPrikaz("${iteracia}. iteracia cyklu: while!"))
            while (poradieVykonanehoPrikazu < pocetPrikazov) {
                var prikaz = zoznamPrikazov.get(poradieVykonanehoPrikazu)

                while (prikaz == ";" || prikaz == "{") {
                    poradieVykonanehoPrikazu++
                    prikaz = zoznamPrikazov[poradieVykonanehoPrikazu].trim()
                }

                if (prikaz == "}") {
                  //  pocetPrikazovVCykle = poradieVykonanehoPrikazu
                   // println("Takto to vyzera $poradieVykonanehoPrikazu | ${zistiMiKolKoPrikazovMamVsebe()}")
                    poradieVykonanehoPrikazu = 0
                    break
                }
               // println(prikaz)
                manazujPrikaz(prikaz)
                poradieVykonanehoPrikazu++


                if (breakContinueNull() == TypKodovehoBloku.BREAK || BREAKorCONTINUEorNULL == TypKodovehoBloku.BREAK) {
                    brk = true
                    BREAKorCONTINUEorNULL = null
                    break
                } else if (breakContinueNull() == TypKodovehoBloku.CONTINUE || BREAKorCONTINUEorNULL == TypKodovehoBloku.CONTINUE) {
                    poradieVykonanehoPrikazu = 0
                    BREAKorCONTINUEorNULL = null
                    break
                }

            }
        }

        hotovePrikazy.add(PrikazVystupu("Vystupujeme z cyklu while! ", "cyklus"))
        return pocetPrikazovVCykle
    }

    override fun vyhodnotKod(): String {
        return """
            Vstupujem to cyklu while.
            Moja podmienka je: $podmienka
        """
    }
}