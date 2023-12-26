package com.example.podgotovka_modul

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val  button_reg: Button = findViewById(R.id.reg_button)
        button_reg.setOnClickListener {
            val next = Intent(this,RegActivity::class.java)
            startActivity(next)
            finish()
        }
    }
}