@file:Suppress("unused")

package com.thor.storyapp

import com.thor.storyapp.data.local.daoModule
import com.thor.storyapp.data.remote.serviceModule
import com.thor.storyapp.repository.repositoryModule
import com.thor.storyapp.ui.viewModelModule
import org.koin.core.module.Module

class App : com.thor.storyapp.base.BaseApplication() {
    override fun defineDependencies(): List<Module> {
        return listOf(
            daoModule,
            serviceModule,
            repositoryModule,
            viewModelModule
        )
    }
}