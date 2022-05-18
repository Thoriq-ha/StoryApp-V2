package com.thor.storyapp.data.remote.response

import com.google.gson.annotations.SerializedName
import com.thor.storyapp.data.local.story.story_entity.StorySchema
import io.reactivex.Observable

data class StoryResponse(
    @SerializedName("error")
    val error: Boolean,

    @SerializedName("message")
    val message: String,

    @SerializedName("listStory")
    val listStory: List<StorySchema> = emptyList()
)

fun StoryResponse.mapToList() = Observable.just(this.listStory)
