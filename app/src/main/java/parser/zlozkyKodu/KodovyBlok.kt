package com.example.visualizationofcode.ui.theme.zloky.kodu

import org.example.zlozkyKodu.*

abstract class KodovyBlok(var zoznamPrikazov: List<String>) {
    var hotovePrikazy: MutableList<CastKodu> = mutableListOf()
    var premenne: MutableList<Array<Any?>?> = mutableListOf()    //datovy typ, nazov, hodnota
    var BREAKorCONTINUEorNULL: TypKodovehoBloku? = null
    constructor() : this(mutableListOf())

    fun pridajPrikazyAPremenne(prikazy: List<String>, prm: MutableList<Array<Any?>?>) {
        zoznamPrikazov = prikazy
        pocetPrikazov = zoznamPrikazov.size
        premenne.addAll(prm)
    }

    var pocetPrikazov: Int = zoznamPrikazov.size
    var poradieVykonanehoPrikazu: Int = 0
    var vnarameSa: Boolean = false


    fun posunSaODalsiePrikazy(oKolko: Int) {
        poradieVykonanehoPrikazu += oKolko
    }

    fun manazujPrikaz(prikaz: String) {
        vnarameSa = when (zistiOAkyTypKoduSaJedna(prikaz)) {
            TypKodovehoBloku.FOR -> {
                  CyklusFor.rozborPrikazu(this, premenne)
            }
            TypKodovehoBloku.WHILE -> {
                  CyklusWhile.rozborPrikazu(this)
            }
            TypKodovehoBloku.DO_WHILE -> {
                  CyklusDoWhile.rozborPrikazu(this)
            }
            TypKodovehoBloku.PREMENNA -> {
                  Premenna.rozborPrikazu(this, prikaz)
            }
            TypKodovehoBloku.VYRAZ, TypKodovehoBloku.VYRAZ_ID -> {
                  Vyraz.rozborPrikazu(this, prikaz)
            }
            TypKodovehoBloku.VYPIS, TypKodovehoBloku.VYPIS_NOVY_RIADOK -> {
                  Vypis.rozborPrikazu(this, prikaz)
            }
            TypKodovehoBloku.IF -> {
                  IF.rozborPrikazu(this, prikaz)
            }
            TypKodovehoBloku.BREAK -> {
                hotovePrikazy.add(Break())
                false
            }
            TypKodovehoBloku.CONTINUE -> {
                hotovePrikazy.add(Continue())
                false
            }
            else -> return
        }

        if (vnarameSa) {
           // println(hotovePrikazy.get(hotovePrikazy.size - 1).vyhodnotKod())
            var vnorenyKodovyBlok: KodovyBlok = hotovePrikazy.get(hotovePrikazy.size - 1) as KodovyBlok

            poradieVykonanehoPrikazu += vnorenyKodovyBlok.spracujPrikazy()

            if (vnorenyKodovyBlok.BREAKorCONTINUEorNULL == TypKodovehoBloku.BREAK) {
                BREAKorCONTINUEorNULL = TypKodovehoBloku.BREAK
            } else if (vnorenyKodovyBlok.BREAKorCONTINUEorNULL == TypKodovehoBloku.CONTINUE) {
                BREAKorCONTINUEorNULL = TypKodovehoBloku.CONTINUE
            }

         //   zoznamPrikazov.slice(poradieVykonanehoPrikazu until zoznamPrikazov.size).forEach { println(it) }
            vnarameSa = false
        }
    }

    fun najdiMiNazovNastavovanejPremennejZVyrazu(vyraz: String): Array<Any?>? {
        val regex = Regex("([a-zA-Z_][a-zA-Z0-9_]*)\\s*=\\s*(.+)")
        var match = regex.find(vyraz.trim())
        var nazov: String? = match?.groups?.get(1)?.value

        if (nazov != null) {
            for (i in premenne.indices) {
                var premenna: Array<Any?>? = premenne[i]
                if (premenna != null && premenna[1] == nazov) {
                    return premenna
                }
            }
        }
        return null
    }

    fun breakContinueNull(): TypKodovehoBloku? {
        if (hotovePrikazy[hotovePrikazy.size - 1] is Continue) {
            return TypKodovehoBloku.CONTINUE
        } else if (hotovePrikazy[hotovePrikazy.size - 1] is Break) {
            return TypKodovehoBloku.BREAK
        } else {
            return null
        }
    }


     fun zistiOAkyTypKoduSaJedna(kod: String): TypKodovehoBloku? {
        val kodTrim = kod.trim()  // Odstránime prebytočné medzery

        when {
            listOf("String", "int", "double", "byte", "short", "float", "boolean", "char")
                .any { kodTrim.startsWith(it) } -> return TypKodovehoBloku.PREMENNA
            kodTrim.startsWith("do") -> return TypKodovehoBloku.DO_WHILE
            kodTrim.startsWith("if") -> return TypKodovehoBloku.IF
            kodTrim.startsWith("for") -> return TypKodovehoBloku.FOR
            kodTrim.startsWith("break") -> return TypKodovehoBloku.BREAK
            kodTrim.startsWith("continue") -> return TypKodovehoBloku.CONTINUE
            kodTrim.startsWith("while") -> return TypKodovehoBloku.WHILE
            kodTrim.startsWith("System") -> {
                return if (kodTrim.contains("println")) TypKodovehoBloku.VYPIS_NOVY_RIADOK else TypKodovehoBloku.VYPIS
            }
        }

        // Priraďovací výraz (podporuje zátvorky)
        val regexPriradenie = Regex("([a-zA-Z_][a-zA-Z0-9_]*)\\s*=\\s*(.+)")
        if (regexPriradenie.matches(kodTrim)) {
            return TypKodovehoBloku.VYRAZ
        }

        // Inkrementačný/dekrementačný výraz (podporuje zátvorky)
        val regexInkrementacia = Regex("([+\\-]{2})?([a-zA-Z_][a-zA-Z0-9_]*)\\s*([+\\-*/%]=|\\+\\+|--)\\s*(.+)?")
        if (regexInkrementacia.matches(kodTrim)) {
            return TypKodovehoBloku.VYRAZ_ID
        }

        return null
    }

    open fun spracujPrikazy(): Int{
        while (poradieVykonanehoPrikazu < pocetPrikazov) {
            var prikaz = zoznamPrikazov[poradieVykonanehoPrikazu].trim()
            while (prikaz == ";" || prikaz == "{" || prikaz == "}") {
                poradieVykonanehoPrikazu++
                if (poradieVykonanehoPrikazu >= pocetPrikazov) {
                    hotovePrikazy.add(InformativnyPrikaz("Program sa skončil!"))
                    return -1
                }
                prikaz = zoznamPrikazov[poradieVykonanehoPrikazu].trim()
            }
           // println(prikaz)
            manazujPrikaz(prikaz)
            poradieVykonanehoPrikazu++
        }
        return -1
    }

    fun vypisVyhodnotenePrikazy() {
        for (i in hotovePrikazy.indices) {
            println(hotovePrikazy[i].vyhodnotKod())
            if (hotovePrikazy[i] is KodovyBlok) {
                (hotovePrikazy[i] as KodovyBlok).vypisVyhodnotenePrikazy()
            }
        }
    }
}
