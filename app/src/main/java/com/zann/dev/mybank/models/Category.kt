package com.zann.dev.mybank.models

import java.io.Serializable

data class Category(
    val title: String,
    val accountList: List<Account> = emptyList(),
    val totalAccount: Int = accountList.size,
    val totalPrice: Double = 0.0
): Serializable