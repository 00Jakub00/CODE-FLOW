package com.example.visualizationofcode.ui.theme.zloky.kodu

import org.example.zlozkyKodu.InformativnyPrikaz
import parser.zlozkyKodu.PrikazVystupu

class IF(var podmienka: String) : KodovyBlok(), CastKodu {

    var nastaloVnorenie: Boolean = false
    var vetvaCislo: Int = 0

    companion object  {
        fun spracovaniePrikazuIF(kod: String): IF {
            var podmienka = Vyraz.nabalVyraz(kod, true)
            return IF(podmienka)
        }

         fun rozborPrikazu(kodovyBlok: KodovyBlok, prikaz: String): Boolean {
            var iffko: IF = spracovaniePrikazuIF(prikaz)
            kodovyBlok.posunSaODalsiePrikazy(1)
            var novePrikazy: List<String> = kodovyBlok.zoznamPrikazov.slice(kodovyBlok.poradieVykonanehoPrikazu until kodovyBlok.zoznamPrikazov.size)

            iffko.pridajPrikazyAPremenne(novePrikazy, kodovyBlok.premenne)
            kodovyBlok.hotovePrikazy.add(iffko)
            return true
        }
    }

    override fun spracujPrikazy(): Int {
        val zatvorky: Array<Int> = arrayOf(0, 0)
        var prikaz = ""

       // println(poradieVykonanehoPrikazu)
       // println(zoznamPrikazov.size)
       // zoznamPrikazov.forEach { println(it) }

        do {
            if (!prikaz.startsWith("else if") && !prikaz.startsWith("else") && prikaz.isNotEmpty()) {
                break
            } else if(prikaz.startsWith("else if") && !nastaloVnorenie) {
                podmienka = Vyraz.nabalVyraz(prikaz, true)
                vetvaCislo++
            } else if(prikaz.startsWith("else") && !nastaloVnorenie) {
                podmienka = "Else vetva"
                vetvaCislo++
            }

            if ((podmienka == "Else vetva" && !nastaloVnorenie) || (!nastaloVnorenie && Vyraz.vyhodnotVyraz(podmienka, premenne) as Boolean )) {
                vetvaCislo++
                nastaloVnorenie = true
                while (poradieVykonanehoPrikazu < pocetPrikazov) {
                    var prikaz = zoznamPrikazov.get(poradieVykonanehoPrikazu)

                    while (prikaz == ";" || prikaz == "{") {
                        poradieVykonanehoPrikazu++
                      //  println(poradieVykonanehoPrikazu)
                        prikaz = zoznamPrikazov[poradieVykonanehoPrikazu].trim()
                    }

                    if (prikaz == "}") {
                        poradieVykonanehoPrikazu++
                      //  println(poradieVykonanehoPrikazu)
                        break
                    }
                 //   println(prikaz)
                    manazujPrikaz(prikaz)   //tuto

                    if ((hotovePrikazy.size > 0 && breakContinueNull() == TypKodovehoBloku.BREAK) || BREAKorCONTINUEorNULL == TypKodovehoBloku.BREAK) {
                        BREAKorCONTINUEorNULL = TypKodovehoBloku.BREAK
                        return 0
                    } else if ((hotovePrikazy.size > 0 && breakContinueNull() == TypKodovehoBloku.CONTINUE) || BREAKorCONTINUEorNULL == TypKodovehoBloku.CONTINUE) {
                        BREAKorCONTINUEorNULL = TypKodovehoBloku.CONTINUE
                        return 0
                    }

                    poradieVykonanehoPrikazu++
                   // println(poradieVykonanehoPrikazu)
                }
            } else {
                do {
                    prikaz = zoznamPrikazov.get(poradieVykonanehoPrikazu)

                    if (prikaz == "{") {
                        zatvorky[0]++
                    } else if (prikaz == "}") {
                        zatvorky[1]++
                    }

                   // println(zatvorky[0])
                   // println(zatvorky[1])

                    poradieVykonanehoPrikazu++
                    //println(poradieVykonanehoPrikazu)
                   // println(zatvorky[0] != zatvorky[1] && zatvorky[0] > 0)
                } while (zatvorky[0] != zatvorky[1] || zatvorky[0] == 0)
                //println("Vystupujem z do while so zatvorkami")
                zatvorky[0] = 0
                zatvorky[1] = 0
            }
            if (poradieVykonanehoPrikazu >= pocetPrikazov) {
                break
            }
            prikaz = zoznamPrikazov.get(poradieVykonanehoPrikazu)
        } while (true)

        if (nastaloVnorenie) {
            hotovePrikazy.add(PrikazVystupu("Vynaram sa z vetvy s pomienkou: $podmienka"))
        } else {
            vetvaCislo = 0
        }
      //  println("Vraciam $poradieVykonanehoPrikazu prikazov")
        return poradieVykonanehoPrikazu - 1
    }

    override fun vyhodnotKod(): String {
        if (nastaloVnorenie) {
            if (podmienka == "Else vetva") {
                return """
                Vstupujem do vetvy poslednej 
                možnosti 
            """
            } else {
                return """
                Vstupujem do vetvy kodu
                kde sa podmienka $podmienka
                vyhodnotila ako pravdivá.
            """
            }
        }
        return """
            Vynechávam vetvu/vetvy.
            Neboli splenené podmienky
        """
    }
}