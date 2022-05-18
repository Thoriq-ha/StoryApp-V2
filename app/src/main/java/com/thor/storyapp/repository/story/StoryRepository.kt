package com.thor.storyapp.repository.story

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.rxjava2.flowable
import com.thor.storyapp.data.local.story.StoryDatabase
import com.thor.storyapp.data.local.story.story_entity.StorySchema
import com.thor.storyapp.data.remote.services.StoryServices
import io.reactivex.Flowable
import okhttp3.MultipartBody
import okhttp3.RequestBody

class StoryRepository(
    private val dao: StoryDatabase, private val service: StoryServices
) {
    fun listMap(token: String) = service.maps(token)

    fun upload(
        token: String,
        file: MultipartBody.Part,
        description: RequestBody,
        lat: RequestBody?,
        lon: RequestBody?
    ) =
        service.uploadImage(token, file, description, lat, lon)

    fun getStory(token: String): Flowable<PagingData<StorySchema>> {
        @OptIn(ExperimentalPagingApi::class)
        return Pager(
            config = PagingConfig(
                pageSize = 10,
                enablePlaceholders = false,
                maxSize = 20,
                prefetchDistance = 5,
                initialLoadSize = 20
            ),
            remoteMediator = StoryRemoteMediator(
                service,
                dao,
                token
            ),
            pagingSourceFactory = {
                dao.story().getAllStory()
            }
        ).flowable
    }

}