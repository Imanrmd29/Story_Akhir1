package com.iman.story_akhir1.core.data.remote.model

import com.google.gson.annotations.SerializedName

data class EmailRespon(
    @field:SerializedName("data")
    val data: List<Any?>? = null,

    @field:SerializedName("message")
    val message: String? = null,

    @field:SerializedName("status")
    val status: Boolean? = null
)
