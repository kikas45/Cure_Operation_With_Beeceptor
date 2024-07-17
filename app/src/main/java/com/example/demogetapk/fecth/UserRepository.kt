package com.example.demogetapk.data

import com.example.demogetapk.fecth.RetrofitInstanceGet
import com.example.demogetapk.fecth.UserModel
import com.example.demogetapk.fecth.UserService
import retrofit2.Response

class UserRepository {

    private val userService = RetrofitInstanceGet.createService(UserService::class.java)

    suspend fun getUsers(): Response<List<UserModel>> {
        return userService.getUsers()
    }

    suspend fun deleteUser(id: String): Response<Void> {
        return userService.deleteUser(id)
    }
}
