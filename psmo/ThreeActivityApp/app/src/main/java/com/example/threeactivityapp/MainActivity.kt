package com.example.threeactivityapp

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    // Register launcher for SecondActivity
    private val secondLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val value = result.data?.getStringExtra("result")?.takeIf { it.isNotEmpty() } ?: "No result"
            findViewById<TextView>(R.id.tvResult).text = "From Second: $value"
        }
    }

    // Register launcher for ThirdActivity
    private val thirdLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val value = result.data?.getStringExtra("result")?.takeIf { it.isNotEmpty() } ?: "No result"
            findViewById<TextView>(R.id.tvResult).text = "From Third: $value"
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val etInput = findViewById<EditText>(R.id.etInput)
        if (savedInstanceState != null) {
            etInput.setText(savedInstanceState.getString("input", ""))
        }
        val tvName = findViewById<TextView>(R.id.tvName)
        tvName.text = "1st Activity"
        val tvResult = findViewById<TextView>(R.id.tvResult)
        tvResult.text = "No results yet"


        val btnStart2 = findViewById<Button>(R.id.btnStart2)
        btnStart2.setOnClickListener {
            val inputMain = etInput.text.toString()
            val intent = Intent(this, Activity2::class.java)
            intent.putExtra("input", inputMain)
            secondLauncher.launch(intent)
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
        }
        val btnStart3 = findViewById<Button>(R.id.btnStart3)
        btnStart3.setOnClickListener {
            val inputMain = etInput.text.toString()
            val intent = Intent(this, Activity3::class.java)
            intent.putExtra("input", inputMain)
            thirdLauncher.launch(intent)
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
        }

    }
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        val etInput = findViewById<EditText>(R.id.etInput)
        outState.putString("input", etInput.text.toString())
    }
}
