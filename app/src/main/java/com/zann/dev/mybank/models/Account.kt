package com.zann.dev.mybank.models

import java.io.Serializable

data class Account(
    val title: String,
    val day: String,
    val month: String,
    val year: String,
    val date: String = "$day/$month/$year",
    val price: Double
): Serializable
