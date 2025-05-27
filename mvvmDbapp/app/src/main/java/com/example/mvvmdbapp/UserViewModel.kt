package com.example.mvvmdbapp

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class UserViewModel(
    application: Application,
    private val repository: UserRepository
) : AndroidViewModel(application) {

    val allUsers: LiveData<List<User>> = repository.allUsers

    fun insert(user: User) = viewModelScope.launch(Dispatchers.IO) {
        repository.insert(user)
    }

    fun update(user: User) = viewModelScope.launch(Dispatchers.IO) {
        repository.update(user)
    }

    fun delete(user: User) = viewModelScope.launch(Dispatchers.IO) {
        repository.delete(user)
    }
}
// This ViewModel class is responsible for managing UI-related data in a lifecycle-conscious way.
// It interacts with the UserRepository to perform data operations and exposes LiveData for observing user data changes.
// The viewModelScope is used to launch coroutines that will be automatically canceled when the ViewModel is cleared.
// The Dispatchers.IO context is used for database operations to ensure they run on a background thread, preventing UI blocking.
// This code is part of a Room database implementation in an Android application.
