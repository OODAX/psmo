package com.example.mvvmdbapp

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface UserDao {
    @Query("SELECT * FROM users ORDER BY id DESC")
    fun getAllUsers(): LiveData<List<User>>

    @Insert
    suspend fun insertUser(user: User)

    @Update
    suspend fun updateUser(user: User)

    @Delete
    suspend fun deleteUser(user: User)
}
// This interface defines the Data Access Object (DAO) for the User entity.
// It includes methods to retrieve all users, insert a new user, update an existing user, and delete a user.
// The @Dao annotation indicates that this interface is a DAO.
// The @Query annotation is used to define SQL queries.
// The @Insert, @Update, and @Delete annotations are used to define methods for inserting, updating, and deleting entities in the database.
// The LiveData type is used to observe changes in the user list, allowing the UI to update automatically when the data changes.
// This code is part of a Room database implementation in an Android application.