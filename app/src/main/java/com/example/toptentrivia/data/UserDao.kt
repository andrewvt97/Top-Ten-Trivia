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

    // check for users who have logged in yesterday or today (active streak) and order them
    @Query("SELECT * FROM users WHERE lastVisitDate = :today OR lastVisitDate = :yesterday ORDER BY streak DESC")
    suspend fun getActiveUsersByStreak(today: String, yesterday: String): List<User>

    // find the top performing users
    @Query("SELECT * FROM users ORDER BY totalPoints DESC")
    suspend fun getUsersByTotalPoints(): List<User>

    // find the top performers today
    @Query("SELECT * FROM users WHERE lastVisitDate = :today ORDER BY pointsToday DESC")
    suspend fun getUsersByPointsToday(today: String): List<User>
}