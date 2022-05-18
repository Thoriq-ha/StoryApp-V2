package com.thor.storyapp.ui.main.location

import com.thor.storyapp.data.local.story.story_entity.StorySchema
import io.reactivex.Observable

interface MapsUseCase {

    fun list(token: String): Observable<List<StorySchema>>

}