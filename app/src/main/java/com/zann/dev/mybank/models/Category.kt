package com.zann.dev.mybank.models

import com.zann.dev.mybank.utils.PriceUtils
import java.io.Serializable

data class Category(
    val title: String,
    val accountList: MutableList<Account> = mutableListOf(),
    val totalAccount: Int = accountList.size,
    var totalPrice: Double = PriceUtils.getTotalPrice(accountList)
): Serializable