package com.thor.storyapp.ui.main.upload

import com.thor.storyapp.data.remote.response.FileUploadResponse
import io.reactivex.Observable
import okhttp3.MultipartBody
import okhttp3.RequestBody

interface UploadUseCase {
    fun upload(
        token: String,
        file: MultipartBody.Part,
        description: RequestBody,
        lat: RequestBody?,
        lon: RequestBody?
    ): Observable<FileUploadResponse>
}