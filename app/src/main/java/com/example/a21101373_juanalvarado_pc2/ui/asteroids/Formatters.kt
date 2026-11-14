package com.example.a21101373_juanalvarado_pc2.ui.asteroids

import java.math.RoundingMode
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.Locale

fun Double?.withUnit(unit: String): String {
    if (this == null) return "N/D"
    val format = DecimalFormat("#,##0.##", DecimalFormatSymbols(Locale.getDefault())).apply {
        roundingMode = RoundingMode.HALF_UP
    }
    return "${format.format(this)} $unit"
}
