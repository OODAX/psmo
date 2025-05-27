package com.example.mvvmdbapp

import androidx.lifecycle.LiveData

class UserRepository(private val userDao: UserDao) {

    val allUsers: LiveData<List<User>> = userDao.getAllUsers()

    suspend fun insert(user: User) = userDao.insertUser(user)
    suspend fun update(user: User) = userDao.updateUser(user)
    suspend fun delete(user: User) = userDao.deleteUser(user)
}
// This repository class abstracts the data operations and provides a clean API for the ViewModel to interact with.
// It uses the UserDao to perform database operations and exposes LiveData for observing user data changes.
// The suspend functions allow for asynchronous operations, ensuring that database access does not block the main thread.
// This code is part of a Room database implementation in an Android application.