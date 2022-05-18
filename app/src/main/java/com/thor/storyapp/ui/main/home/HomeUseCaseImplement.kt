package com.thor.storyapp.ui.main.home

import androidx.paging.PagingData
import com.thor.storyapp.data.local.story.story_entity.StorySchema
import com.thor.storyapp.repository.story.StoryRepository
import io.reactivex.Flowable
import io.reactivex.schedulers.Schedulers


class HomeUseCaseImplement(private val repository: StoryRepository) : HomeUseCase {

    override fun pagingData(token: String): Flowable<PagingData<StorySchema>> =
        repository.getStory(token).subscribeOn(
            Schedulers.io()
        )


}