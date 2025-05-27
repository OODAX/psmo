package com.example.threeactivityapp

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class Activity3 : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_3)

        val tvName = findViewById<TextView>(R.id.tvName)
        val tvInput = findViewById<TextView>(R.id.tvInput)
        val etResult = findViewById<EditText>(R.id.etResult)
        val btnBack = findViewById<Button>(R.id.btnBack)
        if (savedInstanceState != null) {
            etResult.setText(savedInstanceState.getString("result", ""))
        }
        val input = intent.getStringExtra("input")
        tvName.text = "3rd Activity"
        tvInput.text = input?.takeIf { it.isNotEmpty() } ?: "No input"

        btnBack.setOnClickListener {
            val resultIntent = Intent().apply {
                putExtra("result", etResult.text.toString())
            }
            setResult(Activity.RESULT_OK, resultIntent)
            finish()
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString("result", findViewById<EditText>(R.id.etResult).text.toString())
    }
}
