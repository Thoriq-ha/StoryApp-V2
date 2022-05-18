package com.thor.storyapp.ui.auth.register

import com.thor.storyapp.data.remote.response.RegisterResponse
import io.reactivex.Observable

interface RegisterUseCase {

    fun register(
        name: String,
        email: String,
        password: String
    ): Observable<RegisterResponse>
}