package com.thor.storyapp.data.local.story.story_entity

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.thor.storyapp.data.local.story.StoryLocalConfig

@Dao
interface StoryDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertStory(story: List<StorySchema>)

    @Query("SELECT * FROM ${StoryLocalConfig.TABLE_STORY}")
    fun getAllStory(): PagingSource<Int, StorySchema>

    @Query("DELETE FROM ${StoryLocalConfig.TABLE_STORY}")
    fun deleteAll()
}