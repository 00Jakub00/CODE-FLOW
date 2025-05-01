package com.example.visualizationofcode.ui.theme.zloky.kodu

import org.apache.commons.jexl3.JexlBuilder
import org.apache.commons.jexl3.MapContext

class Vyraz (
    var nazovPremennej: String,
    var hodnotaPremennej: Any?
) : CastKodu {

    companion object  {
         fun rozborPrikazu(kodovyBlok: KodovyBlok, prikaz: String): Boolean {
            var spracovavanyPrikaz = prikaz

            if (kodovyBlok.zistiOAkyTypKoduSaJedna(prikaz) == TypKodovehoBloku.VYRAZ_ID) {
                spracovavanyPrikaz = upravVyraz(spracovavanyPrikaz)
            }

            var premenna: Array<Any?>? = kodovyBlok.najdiMiNazovNastavovanejPremennejZVyrazu(spracovavanyPrikaz)

            if (premenna != null) {
                var indexRovnaSa = prikaz.indexOf('=')
                premenna[2] = vyhodnotVyraz(spracovavanyPrikaz.substring(indexRovnaSa + 1, spracovavanyPrikaz.length), kodovyBlok.premenne)
                kodovyBlok.hotovePrikazy.add(Vyraz(premenna[1].toString(), premenna[2]))
            }

            return false
        }

        fun vyhodnotVyraz(vyraz: String, nadradenePremenne: MutableList<Array<Any?>?>): Any? {
            // Vytvorenie JEXL engine
            val jexl = JexlBuilder().create()

            // Pripravenie kontextu (ukladá premenné)
            val context = MapContext()

            // Pridanie všetkých nadradených premenných do kontextu
            nadradenePremenne.forEach { pole ->
                val typ = pole?.getOrNull(0) as? Class<*>
                val nazov = pole?.getOrNull(1) as? String
                val hodnota = pole?.getOrNull(2)

                if (nazov != null && hodnota != null) {
                    context.set(nazov, hodnota)
                }
            }

            // Vytvorenie JEXL výrazu
            val expression = jexl.createExpression(vyraz)
            // Vyhodnotenie výrazu
            return expression.evaluate(context)
        }

        fun upravVyraz(vyraz: String): String {
            val regex = Regex("([a-zA-Z_][a-zA-Z0-9_]*)\\s*(\\+\\+|--|[+\\-*/]=)\\s*(.+)?")
            val match = regex.find(vyraz)
            var upravenyVyraz = ""

            if (match != null) {
                val premenna = match.groupValues[1]
                val operator = match.groupValues[2]
                val hodnota = match.groupValues.getOrNull(3)

                upravenyVyraz += ("$premenna = $premenna ")

                upravenyVyraz += when(operator) {
                    "+=" -> "+ "
                    "-=" -> "- "
                    "*=" -> "* "
                    "/=" -> "/ "
                    "++" -> "+ 1"
                    "--" -> "- 1"
                    else -> ""
                }

                if (hodnota != null) {
                    upravenyVyraz += hodnota
                }

                return upravenyVyraz
            }
            return "Neplatný výraz"
        }

        fun nabalVyraz(kod: String, odseknutPoslednyCharakter: Boolean): String {
            var vyraz = ""
            var zaciname = false
            var a = true

            for (i in kod.indices) {
                if (kod[i] == '(' && a) {
                    zaciname = true
                    a = false
                    continue
                }
                if (zaciname) {
                    vyraz += kod[i]
                }
            }

            if (odseknutPoslednyCharakter) {
                vyraz = vyraz.slice(0 until vyraz.length - 1)
            }
            return vyraz
        }
    }

    override fun vyhodnotKod(): String {
        return """
            Zmenila sa premenna: $nazovPremennej
            Jej nova hodnota je: $hodnotaPremennej
            """
    }
}