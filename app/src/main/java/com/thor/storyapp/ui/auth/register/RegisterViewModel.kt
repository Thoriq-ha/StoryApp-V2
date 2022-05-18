package com.thor.storyapp.ui.auth.register

import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.thor.storyapp.base.BaseViewModel
import com.thor.storyapp.data.remote.response.ErrorResponse
import com.thor.storyapp.data.remote.response.RegisterResponse
import io.reactivex.schedulers.Schedulers
import retrofit2.HttpException
import timber.log.Timber

class RegisterViewModel(private val useCase: RegisterUseCase) : BaseViewModel() {
    private val _stateList = MutableLiveData<RegisterState>()

    val stateList get() = _stateList


    fun register(name: String, email: String, password: String) {
        useCase.register(name, email, password)
            .subscribeOn(Schedulers.io())
            .doOnSubscribe {
                _stateList.postValue(RegisterState.OnLoading)
            }
            .subscribe({
                _stateList.postValue(RegisterState.OnSuccess(it))
            }, {
                if (it is HttpException) {
                    val errorBody = it.response()?.errorBody()
                    val gson = Gson()
                    val type = object : TypeToken<ErrorResponse>() {}.type
                    val errorResponse: ErrorResponse? =
                        gson.fromJson(errorBody!!.charStream(), type)
                    Timber.e(errorResponse?.message)
                    _stateList.postValue(
                        RegisterState.OnError(
                            errorResponse?.message.toString()
                        )
                    )
                }
            }).disposeOnCleared()
    }

}

sealed class RegisterState {
    object OnLoading : RegisterState()
    data class OnSuccess(val uploadResponse: RegisterResponse) : RegisterState()
    data class OnError(val message: String) : RegisterState()
}

