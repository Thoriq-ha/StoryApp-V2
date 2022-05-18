package com.thor.storyapp.repository.story

import android.util.Log
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.rxjava2.RxRemoteMediator
import com.thor.storyapp.data.local.story.StoryDatabase
import com.thor.storyapp.data.local.story.remote_keys_entity.RemoteKeysSchema
import com.thor.storyapp.data.local.story.story_entity.StorySchema
import com.thor.storyapp.data.remote.response.StoryResponse
import com.thor.storyapp.data.remote.services.StoryServices
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import java.io.InvalidObjectException


@OptIn(ExperimentalPagingApi::class)
class StoryRemoteMediator(
    private val service: StoryServices,
    private val database: StoryDatabase,
    private val apiKey: String,
) : RxRemoteMediator<Int, StorySchema>() {

    override fun loadSingle(
        loadType: LoadType,
        state: PagingState<Int, StorySchema>
    ): Single<MediatorResult> {
        return Single.just(loadType)
            .subscribeOn(Schedulers.io())
            .map {
                when (it) {
                    LoadType.REFRESH -> {
                        val remoteKeys = getRemoteKeyClosestToCurrentPosition(state)

                        remoteKeys?.nextKey?.minus(1) ?: 1
                    }
                    LoadType.PREPEND -> {
                        val remoteKeys = getRemoteKeyForFirstItem(state)
                            ?: throw InvalidObjectException("Result is empty")

                        remoteKeys.prevKey ?: INVALID_PAGE
                    }
                    LoadType.APPEND -> {
                        val remoteKeys = getRemoteKeyForLastItem(state)
                            ?: throw InvalidObjectException("Result is empty")
                        remoteKeys.nextKey ?: INVALID_PAGE
                    }
                }
            }
            .flatMap { page ->
                if (page == INVALID_PAGE) {
                    Single.just(MediatorResult.Success(endOfPaginationReached = true))
                } else {
                    service.stories(
                        token = apiKey,
                        page = page,
                        size = state.config.pageSize
                    )
                        .map { insertToDb(page, loadType, it) }
                        .map<MediatorResult> {
                            MediatorResult.Success(endOfPaginationReached = it.listStory.isEmpty())
                        }
                        .onErrorReturn {
                            MediatorResult.Error(it)
                        }
                }
            }
            .onErrorReturn { MediatorResult.Error(it) }

    }

    @Suppress("DEPRECATION")
    private fun insertToDb(page: Int, loadType: LoadType, data: StoryResponse): StoryResponse {
        database.beginTransaction()

        try {
            if (loadType == LoadType.REFRESH) {
                database.remoteKeysDao().deleteRemoteKeys()
                database.story().deleteAll()
            }

            val prevKey = if (page == 1) null else page - 1
            val nextKey = if (data.listStory.isEmpty()) null else page + 1
            val keys = data.listStory.map {
                RemoteKeysSchema(id = it.id, prevKey = prevKey, nextKey = nextKey)
            }
            database.remoteKeysDao().insertAll(keys)
            database.story().insertStory(data.listStory)
            database.setTransactionSuccessful()

        } finally {
            database.endTransaction()
        }

        return data
    }

    private fun getRemoteKeyForLastItem(state: PagingState<Int, StorySchema>): RemoteKeysSchema? {
        return state.pages.lastOrNull { it.data.isNotEmpty() }?.data?.lastOrNull()?.let { repo ->
            database.remoteKeysDao().getRemoteKeysId(repo.id)
        }
    }

    private fun getRemoteKeyForFirstItem(state: PagingState<Int, StorySchema>): RemoteKeysSchema? {
        return state.pages.firstOrNull { it.data.isNotEmpty() }?.data?.firstOrNull()?.let { story ->
            database.remoteKeysDao().getRemoteKeysId(story.id)
        }
    }

    private fun getRemoteKeyClosestToCurrentPosition(state: PagingState<Int, StorySchema>): RemoteKeysSchema? {
        return state.anchorPosition?.let { position ->
            state.closestItemToPosition(position)?.id?.let { id ->
                database.remoteKeysDao().getRemoteKeysId(id)
            }
        }
    }

    companion object {
        const val INVALID_PAGE = -1
    }


}

