package com.example.toptentrivia.ui.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.toptentrivia.data.UserRepository
import com.example.toptentrivia.data.model.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class UserViewModel(
    private val userRepository: UserRepository
) : ViewModel() {

    private val _user = MutableStateFlow<User?>(null)
    val user: StateFlow<User?> = _user

    fun loadUser(username: String) {
        viewModelScope.launch {
            _user.value = userRepository.getUserByUsername(username)
        }
    }

    fun updateUser(user: User) {
        _user.value = user
        viewModelScope.launch { userRepository.updateUser(user) }
    }

    fun clearUser() {
        _user.value = null
    }
}