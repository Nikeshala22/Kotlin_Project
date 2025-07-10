package com.example.fintrack.model

import com.example.fintrack.Transaction

data class BackupData(
    val budgets: List<Budget>,
    val transactions: List<Transaction>
)