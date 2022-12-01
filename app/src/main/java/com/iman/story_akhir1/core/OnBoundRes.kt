package com.iman.story_akhir1.core

import android.util.Log
import com.iman.story_akhir1.core.data.remote.network.ApiRespon
import kotlinx.coroutines.flow.*

abstract class OnlineBoundRes<RequestType> {
    private var result: Flow<Resource<RequestType>> = flow {

        emit(Resource.Loading())
        when (val apiResponse = createCall().first()) {
            is ApiRespon.Success -> {
                getResponse(apiResponse.body)
                emit(Resource.Success(apiResponse.body))
                /* emitAll(
                     getResponse(apiResponse.body).map {
                     Resource.Success(it)
                 })*/
            }
            is ApiRespon.Empty -> {}
            is ApiRespon.Error -> {
                emit(Resource.Error(apiResponse.errorMessage))
                Log.e("OnlineBoundResource", apiResponse.errorMessage)
            }
        }

    }

    protected abstract suspend fun createCall(): Flow<ApiRespon<RequestType>>

    protected abstract fun getResponse(data: RequestType)

    fun asFlow(): Flow<Resource<RequestType>> = result
}