package com.thor.storyapp.ui.auth.register

import com.thor.storyapp.data.remote.response.RegisterResponse
import com.thor.storyapp.repository.auth.AuthRepository
import io.reactivex.Observable

class RegisterUseCaseImplement(private val repository: AuthRepository) : RegisterUseCase {
    override fun register(
        name: String,
        email: String,
        password: String
    ): Observable<RegisterResponse> =
        repository.register(name, email, password)
}
