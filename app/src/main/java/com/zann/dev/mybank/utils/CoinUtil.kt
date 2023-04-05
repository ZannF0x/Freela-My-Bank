package com.zann.dev.mybank.utils

import java.math.RoundingMode
import java.text.DecimalFormat
import java.text.NumberFormat
import java.util.*

object CoinUtil {

    fun doubleToReal(valor: Double): String {
        Locale.setDefault(Locale("pt", "BR"))
        val numberFormat: NumberFormat = DecimalFormat("#,##0.00")
        numberFormat.maximumFractionDigits = 2
        numberFormat.roundingMode = RoundingMode.DOWN
        return "R$ " + numberFormat.format(valor)
    }

}