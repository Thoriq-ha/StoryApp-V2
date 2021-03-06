package com.thor.storyapp.data.local.story.remote_keys_entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "remote_keys")
data class RemoteKeysSchema(
    @PrimaryKey val id: String,
    val prevKey: Int?,
    val nextKey: Int?
)