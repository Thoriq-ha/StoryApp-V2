package com.thor.storyapp.data.local.story.remote_keys_entity

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.thor.storyapp.data.local.story.StoryLocalConfig

@Dao
interface RemoteKeysDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(remoteKeySchema: List<RemoteKeysSchema>)

    @Query("SELECT * FROM ${StoryLocalConfig.TABLE_REMOTE_KEYS} WHERE id = :id")
    fun getRemoteKeysId(id: String): RemoteKeysSchema?

    @Query("DELETE FROM ${StoryLocalConfig.TABLE_REMOTE_KEYS}")
    fun deleteRemoteKeys()
}
