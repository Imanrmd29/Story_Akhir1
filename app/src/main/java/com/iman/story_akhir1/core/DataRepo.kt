package com.iman.story_akhir1.core

import androidx.paging.PagingData
import com.iman.story_akhir1.com.SharedPreferenceProvider
import com.iman.story_akhir1.core.data.local.LocalDatasource
import com.iman.story_akhir1.core.data.local.entity.StoryEntity
import com.iman.story_akhir1.core.data.paging.StoryPagingSource
import com.iman.story_akhir1.core.data.remote.RemoteDatasource
import com.iman.story_akhir1.core.data.remote.model.GeneralRespon
import com.iman.story_akhir1.core.data.remote.model.LoginRespon
import com.iman.story_akhir1.core.data.remote.network.ApiRespon
import kotlinx.coroutines.flow.Flow
import okhttp3.MultipartBody

class DataRepo(
    private val remoteDatasource: RemoteDatasource,
    private val sharedPreferenceProvider: SharedPreferenceProvider,
    private val storyPagingSource: StoryPagingSource,
    private val localDatasource: LocalDatasource
): DataRepoImpl {
    override fun doLogin(email: String, password: String): Flow<Resource<LoginRespon>> {
        return object : OnlineBoundRes<LoginRespon>() {
            override suspend fun createCall(): Flow<ApiRespon<LoginRespon>> {
                return remoteDatasource.doLogin(email, password)
            }

            override fun getResponse(data: LoginRespon) {
                sharedPreferenceProvider.setToken(data.loginResult?.token)
                sharedPreferenceProvider.setUserId(data.loginResult?.userId)
                sharedPreferenceProvider.setName(data.loginResult?.name)
            }
        }.asFlow()
    }

    override fun doRegister(
        email: String,
        password: String,
        name: String
    ): Flow<Resource<GeneralRespon>> {
        return object : OnlineBoundRes<GeneralRespon>() {
            override suspend fun createCall(): Flow<ApiRespon<GeneralRespon>> {
                return remoteDatasource.doRegister(email, password, name)
            }

            override fun getResponse(data: GeneralRespon) {

            }
        }.asFlow()
    }

    override fun addNewStory(
        token: String,
        file: MultipartBody.Part,
        description: String,
        lat: Float,
        lon: Float
    ): Flow<Resource<GeneralRespon>> {
        return object : OnlineBoundRes<GeneralRespon>() {
            override suspend fun createCall(): Flow<ApiRespon<GeneralRespon>> {
                return remoteDatasource.addNewStory(token, file, description, lat, lon)
            }

            override fun getResponse(data: GeneralRespon) {

            }
        }.asFlow()
    }

    override fun getStories(token: String): Flow<PagingData<StoryEntity>> {
        return storyPagingSource.getStories(token)
    }

    override fun getLocalStories(): Flow<List<StoryEntity>> {
        return localDatasource.getStories()
    }
}