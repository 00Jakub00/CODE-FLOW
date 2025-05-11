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
    var referencaFarba = Color(128, 223, 255)

    var aktualnyRiadok = -1

    fun vygenerujMiFarbu() {
        val red = ((referencaFarba.red * 255) * 1.1f).toInt() % 256
        val green = ((referencaFarba.green * 255) * 1.1f).toInt() % 256
        val blue = ((referencaFarba.blue * 255) * 1.1f).toInt() % 256

        referencaFarba = Color(red, green, blue)
    }


    fun dajMiPrvyIndexPoslednehoBloku(): Int {
        return zvyraznene.last().indexy.first()
    }

    fun pridajZvyraznenie(indexy: List<Int>, typBloku: String) {
        vygenerujMiFarbu()
        zvyraznene.add(ZvyrazneneIndexy(indexy, referencaFarba, typBloku))
    }

    fun zmenAktualnyOznacenyRiadok(index: Int) {
        aktualnyRiadok = index
    }

    fun odoberZvyraznenie() {
        zvyraznene.removeAt(zvyraznene.size - 1)
    }

    fun zvyrazniRiadky(textKodu: String): AnnotatedString {
        val riadky = textKodu.lines()
        return buildAnnotatedString {
            riadky.forEachIndexed { index, riadok ->
                val zvyraznenie = zvyraznene.find { index in it.indexy }
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

    fun nakopirujSa(): Zvyraznenie {
        val nova = Zvyraznenie()
        nova.referencaFarba = this.referencaFarba
        nova.odzvyraznitRiadok = this.odzvyraznitRiadok
        nova.aktualnyRiadok = this.aktualnyRiadok
        nova.zvyraznene = this.zvyraznene.map { it.copy() }.toMutableList()
        return nova
    }
}