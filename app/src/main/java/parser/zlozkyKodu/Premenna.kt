package com.example.visualizationofcode.ui.theme.zloky.kodu

class Premenna (
    var datovyTyp: String,
    var nazovPremennej: String,
    var hodnotaPremennej: String
)  : CastKodu {

    companion object  {

         fun rozborPrikazu(kodovyBlok: KodovyBlok, prikaz: String): Boolean {
            var pr: Array<Any?>? = spracovaniePrikazuPremennej(prikaz, kodovyBlok.premenne)
            if (!kodovyBlok.premenne.contains(pr)) {
                kodovyBlok.premenne.add(pr)
            }
            if (pr != null) {
                var datovyTyp = pr[0].toString()
                if (datovyTyp == "class java.lang.String") {
                    datovyTyp = "String"
                }
                kodovyBlok.hotovePrikazy.add(
                    Premenna(
                        datovyTyp,
                        pr[1].toString(),
                        pr[2].toString()
                    )
                )
            }
            return false
        }

        fun spracovaniePrikazuPremennej(retazec: String, nadradenePremenne: MutableList<Array<Any?>?>): Array<Any?>? {
            val premenna: Array<Any?> = arrayOfNulls(3)

            var indexRovnitko = retazec.indexOf('=')

            val slovicka = retazec.substring(0, indexRovnitko).trim().split(" ")
            if (slovicka.size < 2) return null

            premenna[0] = when (slovicka[0]) {
                "String" -> String::class.java
                "int" -> Int::class.java
                "double" -> Double::class.java
                "byte" -> Byte::class.java
                "short" -> Short::class.java
                "float" -> Float::class.java
                "boolean" -> Boolean::class.java
                "char" -> Char::class.java
                else -> return null
            }

            premenna[1] = slovicka[1]

            val vyraz = retazec.substring(indexRovnitko + 1).trim()
            premenna[2] = Vyraz.vyhodnotVyraz(vyraz, nadradenePremenne)

            if (premenna[2] != null) {
                return premenna
            }
            return null
        }
    }

    override  fun vyhodnotKod(): String {
        return """
            Vytvorila sa premenna typu: $datovyTyp
            Jej nazov je: $nazovPremennej
            Jej pociatocna hodnota je: $hodnotaPremennej
            """
    }
}