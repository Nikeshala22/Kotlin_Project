package com.example.fintrack.model

data class Budget(
    val id: Int,
    val name: String,
    val amount: String,
    val startDate: String,
    val endDate: String,
    val note: String,
    val category: String

)