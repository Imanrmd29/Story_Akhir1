package com.iman.story_akhir1.core

import androidx.paging.PagingData
import com.iman.story_akhir1.core.data.local.entity.StoryEntity
import com.iman.story_akhir1.core.data.remote.model.GeneralRespon
import com.iman.story_akhir1.core.data.remote.model.LoginRespon
import kotlinx.coroutines.flow.Flow
import okhttp3.MultipartBody

interface DataRepoImpl {

    fun doLogin(email: String, password: String): Flow<Resource<LoginRespon>>

    fun doRegister(email: String, password: String, name: String): Flow<Resource<GeneralRespon>>

    fun addNewStory(
        token: String,
        file: MultipartBody.Part,
        description: String,
        lat: Float,
        lon: Float
    ): Flow<Resource<GeneralRespon>>

    fun getStories(token: String): Flow<PagingData<StoryEntity>>

    fun getLocalStories(): Flow<List<StoryEntity>>
}