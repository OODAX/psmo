package com.example.fivewidgetsapp

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        supportActionBar?.hide() //

        setContentView(R.layout.activity_main)

        val etName = findViewById<EditText>(R.id.etName)
        etName.setHintTextColor(getColor(R.color.color_hint))
        val btnSubmit = findViewById<Button>(R.id.btnSubmit)
        val cbAgree = findViewById<CheckBox>(R.id.cbAgree)
        val tvWelcome = findViewById<TextView>(R.id.tvWelcome)
        val progressBar = findViewById<ProgressBar>(R.id.progressBar)

        btnSubmit.setOnClickListener {
            val name = etName.text.toString()
            if (cbAgree.isChecked) {
                tvWelcome.text = "Welcome, $name!"
                Toast.makeText(this, "Profile submitted", Toast.LENGTH_SHORT).show()
                progressBar.progress = 100
            } else {
                Toast.makeText(this, "Please agree to the terms", Toast.LENGTH_SHORT).show()
                progressBar.progress = 0
            }
        }
    }
}
