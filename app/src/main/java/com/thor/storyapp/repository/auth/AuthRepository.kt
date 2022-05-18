package com.thor.storyapp.repository.auth

import com.thor.storyapp.data.remote.services.AuthServices

class AuthRepository(private val service: AuthServices) {
    fun login(email: String, password: String) = service.login(email, password)

    fun register(name: String, email: String, password: String) =
        service.register(name, email, password)
}