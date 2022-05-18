package com.thor.storyapp.data.remote.services

import com.thor.storyapp.data.remote.response.FileUploadResponse
import com.thor.storyapp.data.remote.response.StoryResponse
import io.reactivex.Observable
import io.reactivex.Single
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.*

interface StoryServices {
    @Headers("Content-Type: application/json;charset=UTF-8")
    @GET("stories")
    fun stories(
        @Header("Authorization") token: String,
        @Query("page") page: Int,
        @Query("size") size: Int
    ): Single<StoryResponse>

    @Headers("Content-Type: application/json;charset=UTF-8")
    @GET("stories?location=1.")
    fun maps(
        @Header("Authorization") token: String,
    ): Observable<StoryResponse>


    @Multipart
    @POST("stories")
    fun uploadImage(
        @Header("Authorization") token: String,
        @Part file: MultipartBody.Part,
        @Part("description") description: RequestBody,
        @Part("lat") lat: RequestBody?,
        @Part("lon") lon: RequestBody?,
    ): Observable<FileUploadResponse>

}