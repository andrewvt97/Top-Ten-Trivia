package com.example.toptentrivia.ui.screens

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.toptentrivia.data.UserRepository
import com.example.toptentrivia.data.model.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate

class UserViewModel(
    private val userRepository: UserRepository
) : ViewModel() {

    private val _user = MutableStateFlow<User?>(null)
    val user: StateFlow<User?> = _user

    @RequiresApi(Build.VERSION_CODES.O)
    fun loadUser(username: String) {
        viewModelScope.launch {
            _user.value = userRepository.getUserByUsername(username)
            refreshAndUpdateStats()
        }
    }

    // update necessary stats on login
    @RequiresApi(Build.VERSION_CODES.O)
    fun refreshAndUpdateStats() {
        val currentUser = _user.value ?: return
        val today = LocalDate.now().toString()

        if (currentUser.lastVisitDate != today) {
            if (currentUser.lastVisitDate == LocalDate.now().minusDays(1).toString()) {
                currentUser.streak += 1

                if (currentUser.questionsAttemptedToday in 1..9) {
                    currentUser.averagePoints = (
                            (currentUser.averagePoints * (currentUser.gamesPlayedAllTime - 1)) +
                                    currentUser.pointsToday
                            ) / currentUser.gamesPlayedAllTime
                }
            } else {
                currentUser.streak = 1
            }

            currentUser.pointsToday = 0
            currentUser.correctAnswersToday = 0
            currentUser.questionsAttemptedToday = 0
            currentUser.lastVisitDate = today

            updateUser(currentUser)

            _user.value = currentUser
        }
    }

    suspend fun loginUser(username: String, password: String): Result<Boolean> {
        val result = userRepository.loginUser(username, password)
        if (result.isSuccess) {
            _user.value = userRepository.getUserByUsername(username)
        }
        return result.map { true }
    }

    suspend fun registerUser(newUser: User): Result<Boolean> {
        val result = userRepository.registerUser(newUser)
        if (result.isSuccess) {
            _user.value = newUser
        }
        return result.map { true }
    }

    fun incrementScore(score:Int){
        val currentUser = _user.value ?: return

        currentUser.pointsToday += score
        currentUser.totalPoints += score
        currentUser.correctAnswersToday += 1
        currentUser.correctAnswersAllTime += 1
        updateUser(currentUser)
    }

    fun incrementAttempt() {
        val currentUser = _user.value ?: return

        currentUser.questionsAttemptedToday += 1
        currentUser.questionsAttemptedAllTime += 1
        updateUser(currentUser)
    }
    fun incrementOnGameStart() {
        val currentUser = _user.value ?: return

        if (currentUser.questionsAttemptedToday == 0) {
            currentUser.gamesPlayedAllTime += 1
        }

        incrementAttempt()
    }


    fun updateAverage() {
        val currentUser = _user.value ?: return

        if (currentUser.questionsAttemptedToday == 10) {
            val newAverage = (
                    (currentUser.averagePoints * (currentUser.gamesPlayedAllTime - 1)) +
                            currentUser.pointsToday
                    ) / currentUser.gamesPlayedAllTime

            currentUser.averagePoints = newAverage

            updateUser(currentUser)
        }
    }

    private fun updateUser(user: User) {
        _user.value = user
        viewModelScope.launch { userRepository.updateUser(user) }
    }

    fun clearUser() {
        _user.value = null
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun getUserStreakRanking(callback: (Int) -> Unit) {
        val user = _user.value ?: return
        viewModelScope.launch {
            val today = LocalDate.now().toString()
            val yesterday = LocalDate.now().minusDays(1).toString()
            val allUsers = userRepository.getActiveUsersByStreak(today, yesterday)
            val rank = allUsers.indexOfFirst { it.streak == user.streak } + 1
            callback(rank)
        }
    }

    fun getUserTotalPointsRanking(callback: (Int) -> Unit) {
        val user = _user.value ?: return
        viewModelScope.launch {
            val allUsers = userRepository.getUsersByTotalPoints()
            val rank = allUsers.indexOfFirst { it.totalPoints == user.totalPoints } + 1
            callback(rank)
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun getUserTodayRanking(callback: (Int) -> Unit) {
        val user = _user.value ?: return
        viewModelScope.launch {
            val today = LocalDate.now().toString()
            val allUsers = userRepository.getUsersByPointsToday(today)
            val rank = allUsers.indexOfFirst { it.pointsToday == user.pointsToday } + 1
            callback(rank)
        }
    }
    //leaderboard top performers
    private val _topUsersToday = MutableStateFlow<List<User>>(emptyList())
    val topUsersToday: StateFlow<List<User>> = _topUsersToday

    @RequiresApi(Build.VERSION_CODES.O)
    fun fetchTopUsersToday() {
        val today = LocalDate.now().toString()
        viewModelScope.launch {
            val users = userRepository.getUsersByPointsToday(today)
            _topUsersToday.value = users
        }
    }

}