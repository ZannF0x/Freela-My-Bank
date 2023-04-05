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

    fun realToDouble(valor: String?): Double {
        if (valor == null) return 0.0
        val valorReplaced = valor
            .replace("R$", "")
            .replace(" ", "")
            .replace(" ", "")
            .replace(".", "")
            .replace(",", ".")
        return if (valorReplaced.isNotEmpty()) {
            valorReplaced.toDouble()
        } else {
            0.0
        }
    }

}