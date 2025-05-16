package org.example.zlozkyKodu

import com.example.visualizationofcode.ui.theme.zloky.kodu.CastKodu
import com.example.visualizationofcode.ui.theme.zloky.kodu.KodovyBlok
import com.example.visualizationofcode.ui.theme.zloky.kodu.TypKodovehoBloku
import com.example.visualizationofcode.ui.theme.zloky.kodu.Vyraz
import parser.zlozkyKodu.PrikazVystupu

class CyklusDoWhile : CyklickyBlok(), CastKodu {
    var podmienka = ""

    companion object  {
         fun rozborPrikazu(kodovyBlok: KodovyBlok): Boolean {
       //     println("Pouziva sa Do While")
            var cyklusDoWhile = CyklusDoWhile()
            kodovyBlok.posunSaODalsiePrikazy(1)
            var novePrikazy: List<String> = kodovyBlok.zoznamPrikazov.slice(kodovyBlok.poradieVykonanehoPrikazu until kodovyBlok.zoznamPrikazov.size)
            cyklusDoWhile.pridajPrikazyAPremenne(novePrikazy, kodovyBlok.premenne)
            kodovyBlok.hotovePrikazy.add(cyklusDoWhile)
            return true
        }
    }


    override fun spracujPrikazy(): Int {
            var prikaz = ""
            val zatvorky: Array<Int> = arrayOf(0, 0)

            var pocetPrikazovVCykle = zistiMiKolKoPrikazovMamVsebe(false)

            do {
                iteracia++
                hotovePrikazy.add(InformativnyPrikaz("${iteracia}. iteracia cyklu: do while!"))
                poradieVykonanehoPrikazu = 0
                do {
                    prikaz = zoznamPrikazov.get(poradieVykonanehoPrikazu)

                    if (prikaz == "{") {
                        zatvorky[0]++
                    } else if (prikaz == "}") {
                        zatvorky[1]++
                    } else if (prikaz != ";") {
                        manazujPrikaz(prikaz)

                        if (breakContinueNull() == TypKodovehoBloku.BREAK || BREAKorCONTINUEorNULL == TypKodovehoBloku.BREAK) {
                            brk = true
                            BREAKorCONTINUEorNULL = null
                            break
                        } else if (breakContinueNull() == TypKodovehoBloku.CONTINUE || BREAKorCONTINUEorNULL == TypKodovehoBloku.CONTINUE) {
                            poradieVykonanehoPrikazu = pocetPrikazovVCykle
                            BREAKorCONTINUEorNULL = null
                            break
                        }
                    }

                  //  println(zatvorky[0])
                  //  println(zatvorky[1])
                    poradieVykonanehoPrikazu++
                   // println(poradieVykonanehoPrikazu)
                  //  println(zatvorky[0] != zatvorky[1] && zatvorky[0] > 0)
                } while (zatvorky[0] != zatvorky[1] || zatvorky[0] == 0)
                if (brk) {
                    break
                }

                zatvorky[0] = 0
                zatvorky[1] = 0

            //    println("sdadadasd")
                if (podmienka == "") {
                  //  println(Vyraz.nabalVyraz(zoznamPrikazov.get(poradieVykonanehoPrikazu), true))
                    podmienka = Vyraz.nabalVyraz(zoznamPrikazov.get(poradieVykonanehoPrikazu), true)
                }
               // println(podmienka)

                poradieVykonanehoPrikazu++
                val vysledok = Vyraz.vyhodnotVyraz(podmienka, premenne) as Boolean
           //     println("Takto je vyhodnotena podmienka $vysledok")
            } while (vysledok)

            hotovePrikazy.add(PrikazVystupu("Vystupujem z cyklu Do While!", "cyklus"))
            pocetPrikazovVCykle++
            return pocetPrikazovVCykle
        }

    override fun vyhodnotKod(): String {
        return """
            Vstupujem do cyklu Do While
            s podmienkou $podmienka
        """

    }
}