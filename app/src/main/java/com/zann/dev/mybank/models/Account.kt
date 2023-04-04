package com.zann.dev.mybank.models

import java.io.Serializable

data class Account(
    val title: String,
    val date: String,
    val price: Double
): Serializable
