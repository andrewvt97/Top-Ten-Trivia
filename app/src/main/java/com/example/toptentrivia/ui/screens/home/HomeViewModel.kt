package com.example.toptentrivia.ui.screens.home

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.toptentrivia.data.UserRepository
import com.example.toptentrivia.data.model.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate

class HomeViewModel(
    private val userRepository: UserRepository
) : ViewModel() {

    private val _user = MutableStateFlow<User?>(null)
    val user: StateFlow<User?> = _user

    @RequiresApi(Build.VERSION_CODES.O)
    fun loadUser(username: String) {
        viewModelScope.launch {
            val loadedUser = userRepository.getUserByUsername(username)

            if (loadedUser != null) {
                Log.d("HomeViewModel", "Before update: $loadedUser")
                val today = LocalDate.now().toString()
                val lastVisit = loadedUser.lastVisitDate

                if (lastVisit != today) {
                    if (lastVisit == LocalDate.now().minusDays(1).toString()) {
                        loadedUser.streak += 1

                        // Handle partial game played yesterday
                        if (loadedUser.questionsAttemptedToday in 1..9) {
                            loadedUser.averagePoints = (
                                    (loadedUser.averagePoints * (loadedUser.gamesPlayedAllTime - 1)) +
                                            loadedUser.pointsToday
                                    ) / loadedUser.gamesPlayedAllTime
                        }
                    } else {
                        loadedUser.streak = 1
                    }

                    loadedUser.pointsToday = 0.0
                    loadedUser.correctAnswersToday = 0
                    loadedUser.questionsAttemptedToday = 0
                    loadedUser.lastVisitDate = today

                    userRepository.updateUser(loadedUser)
                }
                Log.d("HomeViewModel", "Before update: $loadedUser")
                _user.update { loadedUser }
            }


        }
    }
}