package org.example.zlozkyKodu

import com.example.visualizationofcode.ui.theme.zloky.kodu.CastKodu

class Break : CastKodu {
    override fun vyhodnotKod(): String {
        return "Cyklus sa ukončuje, aplikoval sa príkaz: break!"
    }
}