package com.thor.storyapp.ui.auth.login

import com.thor.storyapp.data.remote.response.LoginResponse
import io.reactivex.Observable

interface LoginUseCase {
    fun login(
        email: String,
        password: String
    ): Observable<LoginResponse>

}