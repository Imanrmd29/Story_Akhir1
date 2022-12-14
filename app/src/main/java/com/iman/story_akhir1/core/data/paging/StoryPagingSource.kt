package com.iman.story_akhir1.core.data.paging

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.iman.story_akhir1.core.data.local.entity.StoryEntity
import com.iman.story_akhir1.core.data.local.room.StoryDatabase
import com.iman.story_akhir1.core.data.remote.network.ApiService
import kotlinx.coroutines.flow.Flow

class StoryPagingSource(
    private val storyDatabase: StoryDatabase,
    private val apiService: ApiService,
) {

    fun getStories(token: String): Flow<PagingData<StoryEntity>> {
        @OptIn(ExperimentalPagingApi::class)
        return Pager(
            config = PagingConfig(
                pageSize = 10
            ),
            remoteMediator =
            StoryRemoteMediator(token, apiService, storyDatabase)
            ,
            pagingSourceFactory = {
                storyDatabase.dao().getPagingStories()
            }
        ).flow
    }

}