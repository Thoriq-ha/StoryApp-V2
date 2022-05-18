package com.thor.storyapp.data.local

import androidx.room.Room
import com.thor.storyapp.data.local.story.StoryDatabase
import com.thor.storyapp.data.local.story.StoryLocalConfig
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module

val daoModule = module {
    factory { get<StoryDatabase>().story() }
    factory { get<StoryDatabase>().remoteKeysDao() }
}

@Volatile
private var INSTANCE: StoryDatabase? = null

val localModule = module {
    single {
        INSTANCE ?: synchronized(this) {
            INSTANCE ?: Room.databaseBuilder(
                androidApplication(),
                StoryDatabase::class.java, StoryLocalConfig.DATABASE_NAME
            )
                .fallbackToDestructiveMigration()
                .build()
                .also { INSTANCE = it }
        }
    }
}
