package com.example.codeflow

import com.example.visualizationofcode.ui.theme.zloky.kodu.HlavnyBlokKodu

class Deterministika {

    var odzvyraznenieRiadku: Boolean = false

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


    fun jePrikazIbaZatvorka(text: String, index: Int): Boolean {
        val lines = text.lines()
        val line = lines[index]
        return line.trim() == "}" || line.trim() == "{"
    }

    fun operaciaKliknutieDoPredu(parser: HlavnyBlokKodu, zvyraznenie: Zvyraznenie, cisloRiadku: Int, textKodu: String): Int {
        if (parser.jeAktualnyPrikazInformativny() && !parser.jePrikazPosledny()) {
            odzvyraznenieRiadku = true
            return zvyraznenie.dajMiPrvyIndexPoslednehoBloku()
        } else if (parser.jeAtualnyPrikazCyklus()) {
            odzvyraznenieRiadku = true
            zvyraznenie.pridajZvyraznenie(najdiIndexyZatvoriekOdIndexu(textKodu, cisloRiadku), "cyklus")
            return cisloRiadku
        } else if (parser.jeAktualnyPrikazKodovyBlok()) {
            odzvyraznenieRiadku = true
            zvyraznenie.pridajZvyraznenie(najdiIndexyZatvoriekOdIndexu(textKodu, cisloRiadku), "kodovy blok")
            return cisloRiadku
        } else if (parser.jeAkualnyPrikazVystupny()) {
            odzvyraznenieRiadku = true
            zvyraznenie.odoberZvyraznenie()
            return cisloRiadku
        } else {
            odzvyraznenieRiadku = false
            zvyraznenie.zmenAktualnyOznacenyRiadok(cisloRiadku)
            return cisloRiadku
        }
    }

    fun operaciaKliknutieDoZadu(parser: HlavnyBlokKodu, zvyraznenie: Zvyraznenie) {
        if (parser.jeAkualnyPrikazVystupny()) {
            odzvyraznenieRiadku = true
            zvyraznenie.odoberOdobraneZvyraznenie()
        } else if (parser.jeAktualnyPrikazKodovyBlok()) {
            zvyraznenie.odoberZvyraznenie()
            odzvyraznenieRiadku = true
        } else if (parser.jeAktualnyPrikazInformativny()) {
            odzvyraznenieRiadku = true
        } else {
            odzvyraznenieRiadku = false
        }
    }
}