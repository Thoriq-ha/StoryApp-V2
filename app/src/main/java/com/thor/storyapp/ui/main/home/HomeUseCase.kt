package com.thor.storyapp.ui.main.home

import androidx.paging.PagingData
import com.thor.storyapp.data.local.story.story_entity.StorySchema
import io.reactivex.Flowable

interface HomeUseCase {
    fun pagingData(token: String): Flowable<PagingData<StorySchema>>
}