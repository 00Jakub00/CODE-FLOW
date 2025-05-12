package com.example.codeflow

import android.util.Log
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle

class Zvyraznenie {
    var zvyraznene: MutableList<ZvyrazneneIndexy> = mutableListOf()

    var odzvyraznitRiadok = false

    var aktualnyRiadok = -1

    fun dajMiPrvyIndexPoslednehoBloku(): Int {
        return zvyraznene.last().indexy.first()
    }

    fun dajMiPoslednyIndexPoslednehoBloku(): Int {
        return zvyraznene.last().indexy.last()
    }

    fun pridajZvyraznenie(indexy: List<Int>, typBloku: String) {
        var novaFarba: FarbyZvyraznenia? = null

        for (farba in FarbyZvyraznenia.values()) {
            novaFarba = farba
            if (!existujeUzTakeZvyraznenie(novaFarba)) {
                break
            }
        }

        if (novaFarba != null) {
            zvyraznene.add(ZvyrazneneIndexy(indexy, novaFarba.toColor(), typBloku, novaFarba))
        }
    }

    fun vymazVsetkyIffyDoPrvehoCyklu(deterministika: Deterministika) {
        while (zvyraznene[zvyraznene.size - 1].typBloku == "if") {
            zvyraznene.removeLastOrNull()
            deterministika.skoky.removeLastOrNull()
        }
    }

    fun zmenAktualnyOznacenyRiadok(index: Int) {
        aktualnyRiadok = index
    }

    fun odoberZvyraznenie() {
        zvyraznene.removeLastOrNull()
    }

    fun zvyrazniRiadky(textKodu: String): AnnotatedString {
        val riadky = textKodu.lines()
        return buildAnnotatedString {
            riadky.forEachIndexed { index, riadok ->
                val zvyraznenie = zvyraznene.reversed().find { index in it.indexy }
                if (zvyraznenie != null) {
                    if (index == aktualnyRiadok) {
                        if (odzvyraznitRiadok) {
                            withStyle(style = SpanStyle(color = zvyraznenie.farba)) {
                                append(riadok + "\n")
                            }
                        } else {
                            withStyle(style = SpanStyle(color = Color(255, 0, 0))) {
                                append(riadok + "\n")
                            }
                        }
                    }
                    else {
                        withStyle(style = SpanStyle(color = zvyraznenie.farba)) {
                            append(riadok + "\n")
                        }
                    }
                } else if (index == aktualnyRiadok && !odzvyraznitRiadok) {
                    withStyle(style = SpanStyle(color = Color(255, 0, 0))) {
                        append(riadok + "\n")
                    }
                } else {
                    append(riadok + "\n")
                }
            }
        }
    }

    fun existujeUzTakeZvyraznenie(farba: FarbyZvyraznenia?): Boolean {
        return zvyraznene.any { it.zvyraznenaFarba == farba }
    }

    fun dajMiTypPoslednehoZvyraznenia(): String {
        return zvyraznene.last().typBloku
    }

    fun nakopirujSa(): Zvyraznenie {
        val nova = Zvyraznenie()
        nova.odzvyraznitRiadok = this.odzvyraznitRiadok
        nova.aktualnyRiadok = this.aktualnyRiadok
        nova.zvyraznene = this.zvyraznene.map { it.copy() }.toMutableList()
        return nova
    }
}