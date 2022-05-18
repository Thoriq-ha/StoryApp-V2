package com.thor.storyapp.ui.main.upload

import androidx.lifecycle.MutableLiveData
import com.thor.storyapp.base.BaseViewModel
import com.thor.storyapp.data.remote.response.FileUploadResponse
import io.reactivex.schedulers.Schedulers
import okhttp3.MultipartBody
import okhttp3.RequestBody

class UploadViewModel(private val useCase: UploadUseCase) : BaseViewModel() {
    private val _stateList = MutableLiveData<UploadState>()

    val stateList get() = _stateList

    fun uploadImage(file: MultipartBody.Part, description: RequestBody, lat: RequestBody?, lon:RequestBody?) {
        useCase.upload(token, file, description, lat, lon)
            .subscribeOn(Schedulers.io())
            .doOnSubscribe {
                _stateList.postValue(UploadState.OnLoading)
            }.subscribe({
                _stateList.postValue(UploadState.OnSuccess(it))
            }, {
                _stateList.postValue(UploadState.OnError(it?.message ?: "Terjadi Kesalahan"))
            }).disposeOnCleared()
    }
}

sealed class UploadState {
    object OnLoading : UploadState()
    data class OnSuccess(val uploadResponse: FileUploadResponse) : UploadState()
    data class OnError(val message: String) : UploadState()
}

