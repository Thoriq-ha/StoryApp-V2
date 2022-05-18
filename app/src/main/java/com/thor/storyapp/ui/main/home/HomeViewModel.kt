package com.thor.storyapp.ui.main.home

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.filter
import androidx.paging.rxjava2.cachedIn
import com.thor.storyapp.base.BaseViewModel
import com.thor.storyapp.data.local.story.story_entity.StorySchema
import com.thor.storyapp.repository.story.StoryRepository
import io.reactivex.Flowable
import io.reactivex.schedulers.Schedulers

class HomeViewModel(
    private val useCase: HomeUseCase,
) : BaseViewModel() {
    private val _stateList = MutableLiveData<HomeState>()

    val stateList get() = _stateList

    fun refresh() {
        getPagingStory()
    }

    private fun getPagingStory() {
        getPagingData()
            .subscribeOn(Schedulers.io())
            .doOnSubscribe {
                _stateList.postValue(HomeState.OnLoading)
            }.subscribe({
                _stateList.postValue(HomeState.OnSuccess(it))
            }, {
                _stateList.postValue(HomeState.OnError(it?.message ?: "Terjadi Kesalahan"))
            }).disposeOnCleared()
    }

    private fun getPagingData(): Flowable<PagingData<StorySchema>> {
        return useCase.pagingData(token)
            .map { pagingData ->
                pagingData.filter {
                    it.photoUrl != null
                }
            }.cachedIn(viewModelScope)

    }
}

sealed class HomeState {
    object OnLoading : HomeState()
    data class OnSuccess(val pagingStory: PagingData<StorySchema>) : HomeState()
    data class OnError(val message: String) : HomeState()
}