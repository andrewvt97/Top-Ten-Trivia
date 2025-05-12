package com.example.toptentrivia.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.toptentrivia.data.model.User

@Database(entities = [User::class], version = 4, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao

    // singleton instance
    companion object {
        @Volatile
        private var Instance: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            // creates database if one doesn't exist
            return Instance ?: synchronized(this) {
                Room.databaseBuilder(
                                context,
                                AppDatabase::class.java,
                                "trivia_database"
                            ).fallbackToDestructiveMigration(false).build().also { Instance = it }
            }
        }
    }
}