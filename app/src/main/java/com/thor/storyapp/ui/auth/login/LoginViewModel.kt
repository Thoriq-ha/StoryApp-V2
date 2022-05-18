package com.thor.storyapp.ui.auth.login

import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.thor.storyapp.base.BaseViewModel
import com.thor.storyapp.data.remote.response.ErrorResponse
import com.thor.storyapp.repository.auth.Login
import io.reactivex.schedulers.Schedulers
import retrofit2.HttpException
import timber.log.Timber


class LoginViewModel(private val useCase: LoginUseCase) : BaseViewModel() {
    private val _stateList = MutableLiveData<LoginState>()

    val stateList get() = _stateList

    fun login(email: String, password: String) {
        useCase.login(email, password)
            .subscribeOn(Schedulers.io())
            .doOnSubscribe {
                _stateList.postValue(LoginState.OnLoading)
            }
            .subscribe(
                {
                    _stateList.postValue(LoginState.OnSuccess(it.loginResult))
                }, {
                    if (it is HttpException) {
                        val errorBody = it.response()?.errorBody()
                        val gson = Gson()
                        val type = object : TypeToken<ErrorResponse>() {}.type
                        val errorResponse: ErrorResponse? =
                            gson.fromJson(errorBody!!.charStream(), type)
                        Timber.e(errorResponse?.message)
                        _stateList.postValue(
                            LoginState.OnError(
                                errorResponse?.message.toString()
                            )
                        )
                    }
                }
            )
            .disposeOnCleared()
    }

}


sealed class LoginState {
    object OnLoading : LoginState()
    data class OnSuccess(val login: Login) : LoginState()
    data class OnError(val message: String) : LoginState()
}
