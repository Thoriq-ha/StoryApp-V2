package com.thor.storyapp.ui.main.upload

import com.thor.storyapp.data.remote.response.FileUploadResponse
import com.thor.storyapp.repository.story.StoryRepository
import io.reactivex.Observable
import okhttp3.MultipartBody
import okhttp3.RequestBody

class UploadUseCaseImplement(private val repository: StoryRepository) : UploadUseCase {
    override fun upload(
        token: String,
        file: MultipartBody.Part,
        description: RequestBody,
        lat: RequestBody?,
        lon: RequestBody?
    ): Observable<FileUploadResponse> =
        repository.upload(token, file, description, lat, lon)
}