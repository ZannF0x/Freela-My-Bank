package com.zann.dev.mybank.utils

import com.zann.dev.mybank.models.Account

object PriceUtils {

    fun getTotalPrice(list: List<Account>): Double {
        var totalPrice = 0.0
        if (list.isEmpty()) {
            return totalPrice
        }
        list.forEach { account ->
            totalPrice += account.price
        }
        return totalPrice
    }

}