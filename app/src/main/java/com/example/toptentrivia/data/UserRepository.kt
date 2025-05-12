package com.example.toptentrivia.data

import com.example.toptentrivia.data.model.User



interface UserRepository {
    suspend fun registerUser(user: User): Result<Unit>
    suspend fun loginUser(username: String, password: String): Result<User>
    suspend fun getUserByUsername(username: String): User?
    suspend fun getUserByEmail(email: String): User?
    suspend fun updateUser(user: User)
}

class OfflineUserRepository(private val userDao: UserDao) : UserRepository {

    // registers user if valid
    override suspend fun registerUser(user: User): Result<Unit> {
        val existingEmail = userDao.getUserByEmail(user.email)
        if (existingEmail != null) return Result.failure(Exception("Email already registered"))

        val existingUsername = userDao.getUserByUsername(user.username)
        if (existingUsername != null) return Result.failure(Exception("Username already exists"))

        userDao.register(user)
        return Result.success(Unit)
    }

    // returns result of User to view model
    override suspend fun loginUser(username: String, password: String): Result<User> {
        val user = userDao.login(username, password)
        return user?.let { Result.success(it) } ?: Result.failure(Exception("Invalid credentials"))
    }


    override suspend fun getUserByUsername(username: String): User? = userDao.getUserByUsername(username)

    override suspend fun getUserByEmail(email: String): User? =
        userDao.getUserByEmail(email)

    override suspend fun updateUser(user: User) = userDao.updateUser(user)
}
