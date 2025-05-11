package com.example.codeflow

import androidx.compose.ui.graphics.Color

enum class FarbyZvyraznenia(val red: Int, val green: Int, val blue: Int) {
    A(51, 128, 204),
    B(204, 51, 204),
    C(89, 204, 51),
    D(89, 204, 51),
    E(128, 0, 128),
    F(0, 255, 255),
    G(255, 255, 255),
    H(0, 0, 0);

    fun toColor(): Color {
        return Color(red, green, blue)
    }
}


