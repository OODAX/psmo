package com.example.mvvmdbapp

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class User(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    var name: String,
    var email: String
)
// This data class represents a User entity in the Room database.
// It includes an auto-generated primary key and fields for name and email.
// This class is part of a Room database implementation in an Android application.

