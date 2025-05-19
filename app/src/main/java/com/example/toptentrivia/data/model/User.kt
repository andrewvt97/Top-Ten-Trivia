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

    var pointsToday: Int = 0, // update after question answered
    var correctAnswersToday: Int = 0, // update after question answered
    var questionsAttemptedToday: Int = 0, // update at start of question
    var streak: Int = 0, // update on login on new day
    var gamesPlayedAllTime: Int = 0, // update on game start
    var averagePoints: Double = 0.0, // update on login on new day
    var totalPoints: Int = 0, // total points
    var correctAnswersAllTime: Int = 0, // update after question answered
    var questionsAttemptedAllTime: Int = 0, // update at start of question
    var topTenFinishes: Int = 0, // unused for now
    var lastVisitDate: String = "" // update on login
)