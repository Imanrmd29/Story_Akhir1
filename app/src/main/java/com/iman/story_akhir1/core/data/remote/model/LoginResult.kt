package com.iman.story_akhir1.core.data.remote.model

import com.google.gson.annotations.SerializedName

data class LoginResult(

    @field:SerializedName("name")
    val name: String? = null,

    @field:SerializedName("userId")
    val userId: String? = null,

    @field:SerializedName("token")
    val token: String? = null
)