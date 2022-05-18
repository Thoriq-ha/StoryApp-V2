package com.thor.storyapp.ui.main.location

import com.thor.storyapp.data.local.story.story_entity.StorySchema
import com.thor.storyapp.data.remote.response.mapToList
import com.thor.storyapp.repository.story.StoryRepository
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers

class MapsUseCaseImplement(private val repository: StoryRepository) : MapsUseCase {

    override fun list(token: String): Observable<List<StorySchema>> {
        return repository.listMap(token).subscribeOn(
            Schedulers.io()
        ).flatMap { response -> response.mapToList() }
    }

}