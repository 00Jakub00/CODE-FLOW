package com.example.codeflow

import android.util.Log
import com.example.visualizationofcode.ui.theme.zloky.kodu.HlavnyBlokKodu
import com.example.visualizationofcode.ui.theme.zloky.kodu.IF
import org.example.zlozkyKodu.InformativnyPrikaz
import parser.zlozkyKodu.PrikazVystupu

class Deterministika {

    val skoky: MutableList<Int> = mutableListOf()

    fun najdiIndexyZatvoriekOdIndexu(text: String, startIndex: Int): List<Int> {
        val lines = text.lines()
        val indices = mutableListOf<Int>()
        var openBraces = 0
        var closeBraces = 0

        for (i in startIndex until lines.size) {
            val line = lines[i]
            openBraces += line.count { it == '{' }
            closeBraces += line.count { it == '}' }
            indices.add(i)

            if (openBraces == closeBraces && openBraces > 0) {
                break
            }
        }
        return indices
    }

    fun dajMiPoslednyIndexPreSkupinuZatvoriek(text: String, startIndex: Int): Int {
        val lines = text.lines()
        val indices = mutableListOf<Int>()
        var openBraces = 0
        var closeBraces = 0

        for (i in startIndex until lines.size) {
            val line = lines[i]
            openBraces += line.count { it == '{' }
            closeBraces += line.count { it == '}' }
            indices.add(i)

            if (openBraces == closeBraces && openBraces > 0) {
                break
            }
        }

        return indices.last()
    }

    fun vypisSkoky() {
        for (i in skoky) {
            Log.d("Skoky", "Skok: $i")
        }
        Log.d("Skoky", "\n")
    }

    fun operaciaVytvaraniaZvyrazneni(parser: HlavnyBlokKodu, zvyraznenie: Zvyraznenie, cisloRiadku: Int, textKodu: String): Int {
        if (parser.jeAktualnyPrikazInformativny() && !parser.bolPrikazPosledny()) {
            zvyraznenie.odzvyraznitRiadok = true
            Log.d("Krokovanie", "Aktualny prikaz: ${parser.dajMiAktualnyPrikaz().vyhodnotKod()}")
            return zvyraznenie.dajMiPrvyIndexPoslednehoBloku()
        } else if (parser.jeAtualnyPrikazCyklus()) {
            zvyraznenie.odzvyraznitRiadok = true
            zvyraznenie.pridajZvyraznenie(najdiIndexyZatvoriekOdIndexu(textKodu, cisloRiadku), "cyklus")
            if (parser.dajMiDalsiPrikaz() is PrikazVystupu) {
                return zvyraznenie.dajMiPoslednyIndexPoslednehoBloku() - 1
            } else {
                return cisloRiadku
            }
        } else if (parser.jeAktualnyPrikazIF()) {
            var iffko: IF = parser.dajMiAktualnyPrikaz() as IF
            var riadky = textKodu.lines()
            var cr = cisloRiadku
            var riadok = cisloRiadku
            var poradie = 1
            zvyraznenie.odzvyraznitRiadok = true

            Log.d("IFOOOOO", "Aktualny riadok pre IFFF: $cisloRiadku")
            do {
                cr = dajMiPoslednyIndexPreSkupinuZatvoriek(textKodu, cr)
                cr++
                if (cr < riadky.size) {
                    Log.d("IF", "Aktualny riadok za IFFF je: ${riadky[cr]}")
                    Log.d("IF", "Zacina s if riadok za IFFF je: ${IF.jePrikazIF(riadky[cr])}")
                }
            } while (cr < riadky.size && IF.jePrikazIF(riadky[cr]))
            cr--
            skoky.add(cr)
          //  vypisSkoky()
          //  Log.d("IF", "Aktualny skok pre IFFF je: $skok")
            if (iffko.vetvaCislo == 0) {
                return skoky.removeLastOrNull()!!
            }
            if (iffko.vetvaCislo == 1) {
                Log.d("Posledny IF", "Riadok je: $riadok")
            }

            while (poradie < iffko.vetvaCislo) {
                riadok = dajMiPoslednyIndexPreSkupinuZatvoriek(textKodu, riadok)
                poradie++
                riadok++
            }
            zvyraznenie.pridajZvyraznenie(najdiIndexyZatvoriekOdIndexu(textKodu, riadok), "if")

            return riadok
        } else if (parser.jeAktualnyPrikazContinue()) {
            zvyraznenie.vymazVsetkyIffyDoPrvehoCyklu(this)
            zvyraznenie.zmenAktualnyOznacenyRiadok(cisloRiadku)
            if (parser.dajMiDalsiPrikaz() is InformativnyPrikaz) {
                zvyraznenie.odzvyraznitRiadok = false
                return zvyraznenie.dajMiPrvyIndexPoslednehoBloku()
            } else {
                zvyraznenie.odzvyraznitRiadok = false
                val pomocna = zvyraznenie.dajMiPoslednyIndexPoslednehoBloku()
                return pomocna - 1
            }
        } else if (parser.jeAktualnyPrikazBreak()) {
            zvyraznenie.vymazVsetkyIffyDoPrvehoCyklu(this)
            zvyraznenie.zmenAktualnyOznacenyRiadok(cisloRiadku)
            zvyraznenie.odzvyraznitRiadok = false
            val pomocna = zvyraznenie.dajMiPoslednyIndexPoslednehoBloku()
            return pomocna - 1
        } else if (parser.jeAkualnyPrikazVystupny()) {
            var typ = zvyraznenie.dajMiTypPoslednehoZvyraznenia()
            zvyraznenie.odzvyraznitRiadok = true
            zvyraznenie.odoberZvyraznenie()

            if (typ == "if") {
                vypisSkoky()
                var hodnota = skoky.removeLastOrNull()!!
                vypisSkoky()
                Log.d("Skoky","Vyskocilo sa na: $hodnota")
                return hodnota
            }
            return cisloRiadku
        } else {
            zvyraznenie.zmenAktualnyOznacenyRiadok(cisloRiadku)
            zvyraznenie.odzvyraznitRiadok = false
            return cisloRiadku
        }
    }
}