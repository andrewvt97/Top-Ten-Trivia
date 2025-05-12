package com.example.toptentrivia.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.toptentrivia.data.model.User

@Dao
interface UserDao {
    // adds user, abort on conflict
    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun register(user: User)

    // find user with username and password
    @Query("SELECT * FROM users WHERE username = :username AND password = :password LIMIT 1")
    suspend fun login(username: String, password: String): User?

    // find user with username
    @Query("SELECT * FROM users WHERE username = :username LIMIT 1")
    suspend fun getUserByUsername(username: String): User?

    //find user with email
    @Query("SELECT * FROM users WHERE email = :email LIMIT 1")
    suspend fun getUserByEmail(email: String): User?


    // update data
    @Update
    suspend fun updateUser(user: User)
}