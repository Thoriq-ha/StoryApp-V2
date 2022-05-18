package com.thor.storyapp.ui.auth.login

import com.thor.storyapp.data.remote.response.LoginResponse
import com.thor.storyapp.repository.auth.AuthRepository
import io.reactivex.Observable

class LoginUseCaseImplement(private val repository: AuthRepository) : LoginUseCase {
    override fun login(email: String, password: String): Observable<LoginResponse> {
        return repository.login(email, password)
    }
}