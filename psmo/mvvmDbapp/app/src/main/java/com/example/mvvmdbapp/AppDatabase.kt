package com.example.mvvmdbapp

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [User::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao

    companion object {
        @Volatile private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "user_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
// This code defines the AppDatabase class for a Room database in an Android application.
// It includes a singleton pattern to ensure that only one instance of the database is created.
// The @Database annotation specifies the entities and version of the database.
// The abstract method userDao() provides access to the UserDao interface, which defines the data access methods.
// The companion object contains a getDatabase method that initializes the database instance using Room's databaseBuilder.
// The synchronized block ensures thread safety when creating the database instance.
// The Volatile annotation ensures that the INSTANCE variable is always read from main memory, preventing stale data issues.
// This code is part of a Room database implementation in an Android application.
