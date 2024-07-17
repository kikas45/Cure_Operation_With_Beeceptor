package com.example.demogetapk.fecth

import retrofit2.Response
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Path

interface UserService {

    @GET("/api/users/")
    suspend fun getUsers(): Response<List<UserModel>>

    @DELETE("/api/users/{id}")
    suspend fun deleteUser(@Path("id") id: String): Response<Void>
}
