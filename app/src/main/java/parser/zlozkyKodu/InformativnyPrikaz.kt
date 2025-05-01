package org.example.zlozkyKodu

import com.example.visualizationofcode.ui.theme.zloky.kodu.CastKodu

class InformativnyPrikaz(var informacia: String): CastKodu {
    override fun vyhodnotKod(): String {
        return informacia
    }
}