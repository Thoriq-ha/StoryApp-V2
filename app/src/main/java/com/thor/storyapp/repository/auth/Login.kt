package com.thor.storyapp.repository.auth

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Login(

	@field:SerializedName("name")
	val name: String? = "",

	@field:SerializedName("userId")
	val userId: String? = "",

	@field:SerializedName("token")
	val token: String? = ""
) : Parcelable
