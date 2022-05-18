package com.thor.storyapp.data.remote

import com.thor.storyapp.BuildConfig
import com.thor.storyapp.data.remote.services.AuthServices
import com.thor.storyapp.data.remote.services.StoryServices
import com.thor.storyapp.utils.RxErrorHandler
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidApplication
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import retrofit2.CallAdapter
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.util.*
import java.util.concurrent.TimeUnit

val serviceModule = module {
    factory { get<Retrofit>().create(StoryServices::class.java) }
    factory { get<Retrofit>().create(AuthServices::class.java) }
}



val remoteModule = module {

    single { File(androidContext().cacheDir, UUID.randomUUID().toString()) }

    single { Cache(get(), (5 * 1024 * 1024)) }

    single {
        HttpLoggingInterceptor()
            .apply {
                level = if (BuildConfig.DEBUG)
                    HttpLoggingInterceptor.Level.BODY
                else
                    HttpLoggingInterceptor.Level.NONE
            }
    }

    single {
        val okhttp: OkHttpClient = OkHttpClient.Builder()
            .cache(get())
            .connectTimeout(60, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .writeTimeout(60, TimeUnit.SECONDS)
            .addInterceptor(get<HttpLoggingInterceptor>())
            .addInterceptor { chain ->
                val request = chain.request().newBuilder()
                chain.proceed(request.build())
            }.build()

        okhttp
    }

    single<Converter.Factory> { GsonConverterFactory.create() }

    single<CallAdapter.Factory> { RxJava2CallAdapterFactory.create() }

    single {
        Retrofit.Builder()
            .client(get())
            .baseUrl(BuildConfig.ENDPOINT)
            .addConverterFactory(get())
            .addCallAdapterFactory(get())
            .build()
    }

}

val errorHandleModule = module {
    single(createdAtStart = true) { RxErrorHandler(androidApplication()) }
}