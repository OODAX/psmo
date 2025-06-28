package com.example.simpledbapp

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import android.view.LayoutInflater

class MainActivity : AppCompatActivity() {

    lateinit var db: DBHelper
    lateinit var listView: ListView
    lateinit var adapter: ArrayAdapter<String>
    var items = mutableListOf<Triple<Int, String, String>>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportActionBar?.hide() // Hide the action bar

        db = DBHelper(this)
        listView = findViewById(R.id.listView)
        val input = findViewById<EditText>(R.id.editText)
        val emailInput = findViewById<EditText>(R.id.editEmail)
        val addButton = findViewById<Button>(R.id.btnAdd)

        refreshList()

        addButton.setOnClickListener {
            val text = input.text.toString()
            val email = emailInput.text.toString()
            if (text.isNotBlank() && email.isNotBlank()) {
                db.insertItem(text, email)
                input.text.clear()
                emailInput.text.clear()
                refreshList()
            } else {
                Toast.makeText(this, "Fill all fields first!", Toast.LENGTH_SHORT).show()
            }
        }

        listView.setOnItemClickListener { _, _, position, _ ->
            val item = items[position]
            showEditDialog(item)
        }

        listView.setOnItemLongClickListener { _, _, position, _ ->
            val item = items[position]
            AlertDialog.Builder(this)
                .setTitle("Delete item")
                .setMessage("Are You sure you want to delete this item?")
                .setPositiveButton("Delete") { _, _ ->
                    db.deleteItem(item.first)
                    refreshList()
                }
                .setNegativeButton("Cancel", null)
                .show()
            true
        }
    }

    private fun showEditDialog(item: Triple<Int, String, String>) {
        val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_edit_item, null)
        val nameEdit = dialogView.findViewById<EditText>(R.id.editName)
        val emailEdit = dialogView.findViewById<EditText>(R.id.editEmail)
        nameEdit.setText(item.second)
        emailEdit.setText(item.third)

        val dialog = AlertDialog.Builder(this)
            .setTitle("Edit item")
            .setView(dialogView)
            .setPositiveButton("Save", null)
            .setNegativeButton("Cancel", null)
            .create()

        dialog.setOnShowListener {
            val button = dialog.getButton(AlertDialog.BUTTON_POSITIVE)
            button.setOnClickListener {
                val newName = nameEdit.text.toString()
                val newEmail = emailEdit.text.toString()
                if (newName.isNotBlank() && newEmail.isNotBlank()) {
                    db.updateItem(item.first, newName, newEmail)
                    refreshList()
                    dialog.dismiss()
                } else {
                    Toast.makeText(this, "Fill all fields first!", Toast.LENGTH_SHORT).show()
                }
            }
        }

        dialog.show()
    }

    private fun refreshList() {
        items = db.getAllItems().toMutableList()
        adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, items.map { "${it.second} (${it.third})" })
        listView.adapter = adapter
    }
}
