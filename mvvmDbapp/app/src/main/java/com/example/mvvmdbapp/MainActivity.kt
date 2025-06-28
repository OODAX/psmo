package com.example.mvvmdbapp

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity(), UserAdapter.OnItemClickListener {

    // ViewModel holds and manages UI-related data
    private val userViewModel: UserViewModel by viewModel()

    private lateinit var etName: EditText
    private lateinit var etEmail: EditText
    private lateinit var btnAdd: Button
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: UserAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportActionBar?.hide() // Hide the action bar

        // UI references
        etName = findViewById(R.id.etName)
        etEmail = findViewById(R.id.etEmail)
        btnAdd = findViewById(R.id.btnAdd)
        recyclerView = findViewById(R.id.rvUsers)

        // Setup RecyclerView
        adapter = UserAdapter(emptyList(), this)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Observe LiveData from ViewModel
        userViewModel.allUsers.observe(this) { users ->
            adapter.updateData(users)
        }

        // Add new user on button click
        btnAdd.setOnClickListener {
            val name = etName.text.toString().trim()
            val email = etEmail.text.toString().trim()

            if (name.isEmpty() || email.isEmpty()) {
                Toast.makeText(this, "Please enter name and email", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val newUser = User(name = name, email = email)
            userViewModel.insert(newUser)

            etName.text.clear()
            etEmail.text.clear()
        }
    }

    // Handle short-click for update
    override fun onItemUpdate(user: User) {
        val dialogView = layoutInflater.inflate(R.layout.dialog_update_user, null)
        val etName = dialogView.findViewById<EditText>(R.id.etUpdateName)
        val etEmail = dialogView.findViewById<EditText>(R.id.etUpdateEmail)

        etName.setText(user.name)
        etEmail.setText(user.email)

        AlertDialog.Builder(this)
            .setTitle("Update User")
            .setView(dialogView)
            .setPositiveButton("Update") { _, _ ->
                val updatedName = etName.text.toString().trim()
                val updatedEmail = etEmail.text.toString().trim()

                if (updatedName.isNotEmpty() && updatedEmail.isNotEmpty()) {
                    user.name = updatedName
                    user.email = updatedEmail
                    userViewModel.update(user)
                }
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    // Handle long-click for delete
    override fun onItemDelete(user: User) {
        AlertDialog.Builder(this)
            .setTitle("Delete User")
            .setMessage("Delete ${user.name}?")
            .setPositiveButton("Yes") { _, _ ->
                userViewModel.delete(user)
            }
            .setNegativeButton("Cancel", null)
            .show()
    }
}
// This MainActivity class serves as the entry point for the application.
// It initializes the UI components, sets up the RecyclerView with an adapter, and observes changes in user data from the ViewModel.
// The activity handles user interactions such as adding, updating, and deleting users.
// The onItemUpdate and onItemDelete methods handle user updates and deletions, respectively, using AlertDialogs for user confirmation.
// The ViewModel is used to manage UI-related data in a lifecycle-conscious way, ensuring that data survives configuration changes.
// This code is part of a Room database implementation in an Android application, enabling efficient data management and UI updates.
