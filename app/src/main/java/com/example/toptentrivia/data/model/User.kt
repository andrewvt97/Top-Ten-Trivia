package com.example.toptentrivia.data.model

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "users",
    indices = [
    Index(value = ["username"], unique = true),
    Index(value = ["email"], unique = true)
]

)
data class User(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val email: String,
    val username: String,
    val password: String,
    val pointsToday: Int = 0,
    val correctAnswersToday: Int = 0,
    val streak: Int = 0,
    val gamesPlayedAllTime: Int = 0,
    val averagePoints: Double = 0.0,
    val correctAnswersAllTime: Int = 0,
    val topTenFinishes: Int = 0,
    val lastVisitDate: String = ""
)