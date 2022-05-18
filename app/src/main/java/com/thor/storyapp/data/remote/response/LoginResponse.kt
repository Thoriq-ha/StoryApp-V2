package com.thor.storyapp.data.remote.response

import com.google.gson.annotations.SerializedName
import com.thor.storyapp.repository.auth.Login

data class LoginResponse(
    @SerializedName("error")
    val error: Boolean,

    @SerializedName("message")
    val message: String,

    @SerializedName("loginResult")
    val loginResult: Login,
)

