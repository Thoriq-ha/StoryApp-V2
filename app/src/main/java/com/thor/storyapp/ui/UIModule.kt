package com.thor.storyapp.ui

import com.thor.storyapp.ui.auth.login.LoginUseCase
import com.thor.storyapp.ui.auth.login.LoginUseCaseImplement
import com.thor.storyapp.ui.auth.login.LoginViewModel
import com.thor.storyapp.ui.auth.register.RegisterUseCase
import com.thor.storyapp.ui.auth.register.RegisterUseCaseImplement
import com.thor.storyapp.ui.auth.register.RegisterViewModel
import com.thor.storyapp.ui.main.home.HomeUseCase
import com.thor.storyapp.ui.main.home.HomeUseCaseImplement
import com.thor.storyapp.ui.main.home.HomeViewModel
import com.thor.storyapp.ui.main.location.MapsUseCase
import com.thor.storyapp.ui.main.location.MapsUseCaseImplement
import com.thor.storyapp.ui.main.location.MapsViewModel
import com.thor.storyapp.ui.main.upload.UploadUseCase
import com.thor.storyapp.ui.main.upload.UploadUseCaseImplement
import com.thor.storyapp.ui.main.upload.UploadViewModel
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    single<HomeUseCase> { HomeUseCaseImplement(get()) }
    viewModel { HomeViewModel(get()) }

    single<UploadUseCase> { UploadUseCaseImplement(get()) }
    viewModel { UploadViewModel(get()) }

    single<RegisterUseCase> { RegisterUseCaseImplement(get()) }
    viewModel { RegisterViewModel(get()) }

    single<LoginUseCase> { LoginUseCaseImplement(get()) }
    viewModel { LoginViewModel(get()) }

    single<MapsUseCase> { MapsUseCaseImplement(get()) }
    viewModel { MapsViewModel(get()) }
}