package com.iman.story_akhir1.core.data.remote.model

import com.google.gson.annotations.SerializedName

data class LoginRespon(

    @field:SerializedName("loginResult")
    val loginResult: LoginResult? = null,

    @field:SerializedName("error")
    val error: Boolean? = null,

    @field:SerializedName("message")
    val message: String? = null
)