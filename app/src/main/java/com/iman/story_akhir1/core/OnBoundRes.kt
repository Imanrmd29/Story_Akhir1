package com.iman.story_akhir1.core

import android.util.Log
import com.iman.story_akhir1.core.data.remote.network.ApiRespon
import kotlinx.coroutines.flow.*

abstract class OnlineBoundRes<RequestType> {
    private var result: Flow<Resource<RequestType>> = flow {

        emit(Resource.Loading())
        when (val apiRespon = createCall().first()) {
            is ApiRespon.Success -> {
                getRespon(apiRespon.body)
                emit(Resource.Success(apiRespon.body))
            }
            is ApiRespon.Empty -> {}
            is ApiRespon.Error -> {
                emit(Resource.Error(apiRespon.errorMessage))
                Log.e("OnlineBoundRes", apiRespon.errorMessage)
            }
        }

    }

    protected abstract suspend fun createCall(): Flow<ApiRespon<RequestType>>

    protected abstract fun getRespon(data: RequestType)

    fun asFlow(): Flow<Resource<RequestType>> = result
}