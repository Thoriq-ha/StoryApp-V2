package com.thor.storyapp.data.local.story

import androidx.room.Database
import androidx.room.RoomDatabase
import com.thor.storyapp.data.local.story.remote_keys_entity.RemoteKeysDao
import com.thor.storyapp.data.local.story.remote_keys_entity.RemoteKeysSchema
import com.thor.storyapp.data.local.story.story_entity.StoryDao
import com.thor.storyapp.data.local.story.story_entity.StorySchema

@Database(
    entities = [
        StorySchema::class,
        RemoteKeysSchema::class
    ], version = 1, exportSchema = false
)
abstract class StoryDatabase : RoomDatabase() {
    abstract fun story(): StoryDao
    abstract fun remoteKeysDao(): RemoteKeysDao
}