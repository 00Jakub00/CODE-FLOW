package parser.zlozkyKodu

import com.example.visualizationofcode.ui.theme.zloky.kodu.CastKodu

class PrikazVystupu(var informacia: String): CastKodu {
    override fun vyhodnotKod(): String {
        return informacia
    }
}