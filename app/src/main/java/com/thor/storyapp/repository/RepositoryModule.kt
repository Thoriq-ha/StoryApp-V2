package com.thor.storyapp.repository

import com.thor.storyapp.repository.auth.AuthRepository
import com.thor.storyapp.repository.story.StoryRepository
import org.koin.dsl.module

val repositoryModule = module {

    single { StoryRepository(get(), get()) }

    single { AuthRepository(get()) }

}