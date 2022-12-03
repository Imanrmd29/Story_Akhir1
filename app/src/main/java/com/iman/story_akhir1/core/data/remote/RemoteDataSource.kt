package com.iman.story_akhir1.core.data.remote

import android.content.Context
import android.util.Log
import com.iman.story_akhir1.R
import com.iman.story_akhir1.core.data.remote.model.GeneralRespon
import com.iman.story_akhir1.core.data.remote.model.LoginRespon
import com.iman.story_akhir1.core.data.remote.model.StoriesRespon
import com.iman.story_akhir1.core.data.remote.network.ApiRespon
import com.iman.story_akhir1.core.data.remote.network.ApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import retrofit2.HttpException
import java.net.SocketTimeoutException

class RemoteDatasource(
    private val apiService: ApiService?,
    private val context: Context
) {

    suspend fun doLogin(email: String, password: String): Flow<ApiRespon<LoginRespon>> {
        return flow {
            try {
                val respon = apiService?.doLogin(email, password)
                if (respon != null) {
                    emit(ApiRespon.Success(respon))
                } else {
                    emit(ApiRespon.Empty)
                }
            } catch (e: Exception) {
                emit(exceptionLog(e, "doLogin"))
            }
        }.flowOn(Dispatchers.IO)
    }

    suspend fun doRegister(
        email: String,
        password: String,
        name: String
    ): Flow<ApiRespon<GeneralRespon>> {
        return flow {
            try {
                val respon = apiService?.doRegister(email, password, name)
                if (respon != null) {
                    emit(ApiRespon.Success(respon))
                } else {
                    emit(ApiRespon.Empty)
                }
            } catch (e: Exception) {
                emit(exceptionLog(e, "doRegister"))
            }
        }.flowOn(Dispatchers.IO)
    }

    suspend fun getStories(
        token: String,
        page: String?,
        size: String?,
        location: String?
    ): Flow<ApiRespon<StoriesRespon>> {
        return flow {
            try {
                val respon = apiService?.getStories(token, page, size, location)
                if (respon != null) {
                    emit(ApiRespon.Success(respon))
                } else {
                    emit(ApiRespon.Empty)
                }
            } catch (e: Exception) {
                emit(exceptionLog(e, "getStories"))
            }
        }.flowOn(Dispatchers.IO)
    }

    suspend fun addNewStory(
        token: String,
        file: MultipartBody.Part,
        description: String,
        lat: Float,
        lon: Float
    ): Flow<ApiRespon<GeneralRespon>> {
        return flow {
            try {
                val respon = apiService?.addNewStory(token, file, description.toRequestBody("text/plain".toMediaType()), lat, lon)
                if (respon != null) {
                    emit(ApiRespon.Success(respon))
                } else {
                    emit(ApiRespon.Empty)
                }
            } catch (e: Exception) {
                emit(exceptionLog(e, "addNewStory"))
            }
        }.flowOn(Dispatchers.IO)
    }

    private fun exceptionLog(e: Exception, tagLog: String): ApiRespon.Error {
        val tag = this::class.java.simpleName

        when (e) {
            is SocketTimeoutException -> {
                Log.e(tag, e.message.toString())
                return ApiRespon.Error(
                    e.message.toString() + ", " + context.resources.getString(
                        R.string.check_your_internet_connection
                    )
                )
            }

            is HttpException -> {
                return try {
                    val `object` = JSONObject(e.response()?.errorBody()?.string().toString())
                    val messageString: String = `object`.getString("message")
                    Log.e(tag, messageString)
                    ApiRespon.Error(messageString)
                } catch (e: Exception) {
                    val messageString = context.resources.getString(R.string.something_wrong)
                    Log.e(tag, messageString)
                    ApiRespon.Error(messageString)
                }
            }

            is NoSuchElementException -> {
                Log.e(tag, e.message.toString())
                return (ApiRespon.Error(
                    e.message.toString() + ", " + context.resources.getString(
                        R.string.check_your_internet_connection
                    )
                ))
            }

            else -> {
                val messageString = context.resources.getString(R.string.something_wrong)
                Log.e(tag, e.message.toString())
                return ApiRespon.Error(messageString)
            }
        }
    }
}