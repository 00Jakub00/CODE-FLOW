package com.example.visualizationofcode.ui.theme.zloky.kodu

import org.example.zlozkyKodu.CyklickyBlok
import org.example.zlozkyKodu.InformativnyPrikaz
import parser.zlozkyKodu.PrikazVystupu

class CyklusFor(val datovyTyp: String,
              val nazovPremennej: String,
              val pociatocnaHodnotaPremennej: Double,
              val porovnavac: String,
              val porovnavaciaHodnota: Double,
              val krok: String) : CyklickyBlok(), CastKodu{

                  init {
                      premenne.add(arrayOf(datovyTyp, nazovPremennej, pociatocnaHodnotaPremennej))
                  }


    companion object  {
         fun rozborPrikazu(
            kodovyBlok: KodovyBlok,
            nadradenePremenne: MutableList<Array<Any?>?>
        ): Boolean {
           // println("Poradie vykonavaneho prikazu: ${kodovyBlok.poradieVykonanehoPrikazu}")
            var cyklusFor: CyklusFor =
                spracovaniePrikazuFor(kodovyBlok.zoznamPrikazov.slice(kodovyBlok.poradieVykonanehoPrikazu until kodovyBlok.poradieVykonanehoPrikazu + 5), nadradenePremenne)
            kodovyBlok.posunSaODalsiePrikazy(6)
           //  println("Poradie vykonavaneho prikazu: ${kodovyBlok.poradieVykonanehoPrikazu}")
            var novePrikazy: List<String> = kodovyBlok.zoznamPrikazov.slice(kodovyBlok.poradieVykonanehoPrikazu until kodovyBlok.zoznamPrikazov.size)

          //   novePrikazy.forEach { println("Novy prikaz pre for: $it") }

            cyklusFor.pridajPrikazyAPremenne(novePrikazy, kodovyBlok.premenne)
            kodovyBlok.hotovePrikazy.add(cyklusFor)

           //  kodovyBlok.zoznamPrikazov.forEach { println("Prikaz pre for: $it") }
            return true
        }

        fun spracovaniePrikazuFor(list: List<String>, nadradenePremenne: MutableList<Array<Any?>?>): CyklusFor {
            var datovyTyp = ""
            var nazovPremennej = ""
            var pociatocnaHodnotaPremennej = 0.0
            var porovnavac = ""
            var porovnavaciaHodnota = 0.0
            var krok = ""

            var hodnotaCislo = 1

            for (i in list.indices) {
                if (list[i] == ";") {
                    continue
                }

                var retazec = list[i]
                var kontrolaPrikazu = ""
                var zaciatokPrikazu = false

                for (j in retazec.indices) {
                    kontrolaPrikazu += retazec[j]

                    when (hodnotaCislo) {
                        1 -> {
                            if (kontrolaPrikazu[kontrolaPrikazu.length - 1] == '(') {
                                kontrolaPrikazu = ""
                                zaciatokPrikazu = true
                            } else if (zaciatokPrikazu) {
                                if (kontrolaPrikazu.length > 1 && kontrolaPrikazu[kontrolaPrikazu.length - 1]
                                    == ' ' && kontrolaPrikazu[kontrolaPrikazu.length - 2] != ' '
                                ) {
                                    kontrolaPrikazu = kontrolaPrikazu.replace(" ", "")

                                    datovyTyp = kontrolaPrikazu
                                    kontrolaPrikazu = ""
                                    hodnotaCislo++
                                }
                            }
                        }

                        2 -> {
                            if (kontrolaPrikazu.length > 1 && (kontrolaPrikazu[kontrolaPrikazu.length - 1]
                                        == ' ' || kontrolaPrikazu[kontrolaPrikazu.length - 1] == '=') &&
                                kontrolaPrikazu[kontrolaPrikazu.length - 2] != ' '
                            ) {

                                kontrolaPrikazu = kontrolaPrikazu.replace(" ", "")
                                kontrolaPrikazu = kontrolaPrikazu.replace("=", "")

                                nazovPremennej = kontrolaPrikazu
                                kontrolaPrikazu = ""
                                hodnotaCislo++;
                            }
                        }

                        3 -> {
                            if (j == retazec.length - 1) {
                                kontrolaPrikazu = kontrolaPrikazu.replace(" ", "")
                                kontrolaPrikazu = kontrolaPrikazu.replace("=", "")
                                pociatocnaHodnotaPremennej = Vyraz.vyhodnotVyraz(kontrolaPrikazu, nadradenePremenne).toString().toDouble()
                               // pociatocnaHodnotaPremennej = kontrolaPrikazu.toString().toDouble()
                                kontrolaPrikazu = ""
                                hodnotaCislo++
                            }
                        }

                        4 -> {
                            if (kontrolaPrikazu[kontrolaPrikazu.length - 1] == '<' || kontrolaPrikazu[kontrolaPrikazu.length - 1] == '>') {
                                var hodnota = "";
                                hodnota += kontrolaPrikazu[kontrolaPrikazu.length - 1]

                                if (retazec[j + 1] == '=') {
                                    hodnota += retazec[j + 1]
                                }
                                porovnavac = hodnota
                                hodnotaCislo++
                                kontrolaPrikazu = ""
                            }
                        }

                        5 -> {
                            if (j == retazec.length - 1) {
                                kontrolaPrikazu = kontrolaPrikazu.replace(" ", "")
                                kontrolaPrikazu = kontrolaPrikazu.replace("=", "")
                                porovnavaciaHodnota = kontrolaPrikazu.toString().toDouble()
                                kontrolaPrikazu = ""
                                hodnotaCislo++
                            }
                        }

                        6 -> {
                            if (j == retazec.length - 2) {
                                krok = Vyraz.upravVyraz(kontrolaPrikazu)
                            }
                        }
                    }
                }
            }
            val cyklusFor =
                CyklusFor(datovyTyp, nazovPremennej, pociatocnaHodnotaPremennej, porovnavac, porovnavaciaHodnota, krok)
            // textview.text = cyklusFor.jff()
            return cyklusFor
        }
    }



    override fun spracujPrikazy(): Int {
        var podmienkaUkoncenia = "$nazovPremennej $porovnavac $porovnavaciaHodnota"
       // zoznamPrikazov.forEach { println(it) }
        var pocetPrikazovVCykle = zistiMiKolKoPrikazovMamVsebe(true)
      //  zoznamPrikazov.forEach { println(it) }

        while(Vyraz.vyhodnotVyraz(podmienkaUkoncenia, premenne) as Boolean) {
            if (brk) {
                BREAKorCONTINUEorNULL = null
                break
            }
            iteracia++
            hotovePrikazy.add(InformativnyPrikaz("${iteracia}. iteracia cyklu: for!"))
            while (poradieVykonanehoPrikazu < pocetPrikazov) {
                var prikaz = zoznamPrikazov.get(poradieVykonanehoPrikazu)

                while (prikaz == ";" || prikaz == "{") {
                    poradieVykonanehoPrikazu++
                    prikaz = zoznamPrikazov[poradieVykonanehoPrikazu].trim()
                }

                if (prikaz == "}") {
                 //   println("dadasdsad")
                 //   println("Takto to vyzera $poradieVykonanehoPrikazu | ${zistiMiKolKoPrikazovMamVsebe(true)}")

                  //  pocetPrikazovVCykle = poradieVykonanehoPrikazu
                    poradieVykonanehoPrikazu = 0
                    break
                }

             //   println(prikaz)

              //  referencnePrikazy = zoznamPrikazov
                manazujPrikaz(prikaz)
               // if (BREAKorCONTINUEorNULL == TypKodovehoBloku.CONTINUE)
               // zoznamPrikazov = referencnePrikazy

                if (breakContinueNull() == TypKodovehoBloku.BREAK || BREAKorCONTINUEorNULL == TypKodovehoBloku.BREAK) {
                    brk = true
                    break
                } else if (breakContinueNull() == TypKodovehoBloku.CONTINUE || BREAKorCONTINUEorNULL == TypKodovehoBloku.CONTINUE) {
                    poradieVykonanehoPrikazu = 0
                    BREAKorCONTINUEorNULL = null
                    break
                }

                poradieVykonanehoPrikazu++
            }

            var iteracnaPremenna: Array<Any?>? = najdiMiNazovNastavovanejPremennejZVyrazu(krok)
            if (iteracnaPremenna != null) {
                var indexRovnaSa = krok.indexOf('=')
                iteracnaPremenna[2] = Vyraz.vyhodnotVyraz(krok.substring(indexRovnaSa + 1, krok.length), premenne)
            }
        }

        hotovePrikazy.add(PrikazVystupu("Vystupujeme z cyklu for!"))
        return pocetPrikazovVCykle
    }

    override fun vyhodnotKod(): String {
        return """
            Vstupujeme do cyklu for!
            Datovy typ: $datovyTyp
            Nazov premennej: $nazovPremennej
            Hodnota premennej: $pociatocnaHodnotaPremennej
            Porovnavac: $porovnavac
            Porovnavacia hodnota: $porovnavaciaHodnota
            Krok: $krok   
        """
    }
}