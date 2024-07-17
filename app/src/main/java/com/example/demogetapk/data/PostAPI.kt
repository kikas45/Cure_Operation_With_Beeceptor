package com.example.demogetapk.data

import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface PostAPI {

    @Headers("Content-Type: application/json")
    @POST("/api/users/")
    suspend fun postData(
        @Body data: Map<String, String>
    ): Response<ResponseBody>


    @Headers("Content-Type: application/json")
    @PUT("/api/users/{id}")
    suspend fun updateUser(
        @Path("id") id: String,
        @Body data: Map<String, String>
    ): Response<ResponseBody>

}
