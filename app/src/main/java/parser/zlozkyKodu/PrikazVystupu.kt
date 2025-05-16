package parser.zlozkyKodu

import com.example.visualizationofcode.ui.theme.zloky.kodu.CastKodu

class PrikazVystupu(
    val informacia: String,
    val typBlokuZKtorehoSaVystupuje: String
) : CastKodu {
    override fun vyhodnotKod(): String {
        return informacia
    }
}