package com.thor.storyapp.ui.main.location

import android.annotation.SuppressLint
import androidx.lifecycle.MutableLiveData
import com.thor.storyapp.base.BaseViewModel
import com.thor.storyapp.data.local.story.story_entity.StorySchema
import io.reactivex.schedulers.Schedulers

class MapsViewModel(private val useCase: MapsUseCase) : BaseViewModel() {
    private val _stateList = MutableLiveData<MapsState>()
    val stateList get() = _stateList

    fun refresh() {
        getLocation()
    }


    private fun getLocation() {
        useCase.list(token)
            .subscribeOn(Schedulers.io())
            .doOnSubscribe {
                _stateList.postValue(MapsState.OnLoading)
            }.subscribe({
                _stateList.postValue(MapsState.OnSuccess(it))
            }, {
                _stateList.postValue(MapsState.OnError(it?.message ?: "Terjadi Kesalahan"))
            }).disposeOnCleared()
    }

}

sealed class MapsState {
    object OnLoading : MapsState()
    data class OnSuccess(val list: List<StorySchema>) : MapsState()
    data class OnError(val message: String) : MapsState()
}
