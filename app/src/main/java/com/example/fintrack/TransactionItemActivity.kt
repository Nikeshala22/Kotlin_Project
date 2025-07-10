package com.example.fintrack

import android.os.Bundle
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity

class TransactionItemActivity : AppCompatActivity() {

    private lateinit var btnEdit: ImageView
    private lateinit var btnDelete: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_transaction_item)


    }
}
