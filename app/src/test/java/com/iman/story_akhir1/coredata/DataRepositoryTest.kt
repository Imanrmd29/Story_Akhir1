package com.iman.story_akhir1.coredata

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.iman.story_akhir1.DataDummy
import com.iman.story_akhir1.MainDispatcherRule
import com.iman.story_akhir1.com.SharedPreferenceProvider
import com.iman.story_akhir1.core.DataRepo
import com.iman.story_akhir1.core.Resource
import com.iman.story_akhir1.core.data.local.LocalDatasource
import com.iman.story_akhir1.core.data.paging.StoryPagingSource
import com.iman.story_akhir1.core.data.remote.RemoteDatasource
import com.iman.story_akhir1.core.data.remote.model.GeneralRespon
import com.iman.story_akhir1.core.data.remote.network.ApiRespon
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.Mockito.verify
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class DataRepositoryTest{

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var remoteDatasource: RemoteDatasource
    @Mock
    private lateinit var localDatasource: LocalDatasource
    @Mock
    private lateinit var storyPagingSource: StoryPagingSource
    @Mock
    private lateinit var sharedPreferenceProvider: SharedPreferenceProvider
    private lateinit var dataRepository: DataRepo

    private val emailMock = "iman@email.com"
    private val passwordMock = "12345678"
    private val nameMock = "bukan iman"
    private val generalResponseMock = DataDummy.generalResponse()

    @Before
    fun setup(){
        dataRepository = DataRepo(remoteDatasource, sharedPreferenceProvider, storyPagingSource, localDatasource)
    }

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()


    @Test
    fun `when register`()  = runTest {
        val expectedResponse : Flow<ApiRespon<GeneralRespon>> = flow {
            emit(ApiRespon.Success(generalResponseMock))
        }
        `when`(remoteDatasource.doRegister(emailMock, passwordMock, nameMock)).thenReturn(
            expectedResponse
        )
        dataRepository.doRegister(emailMock, passwordMock, nameMock).collect {
            if (it !is Resource.Loading){
                verify(remoteDatasource).doRegister(emailMock, passwordMock, nameMock)
                assertNotNull(it.data)
                assertTrue(it is Resource.Success)
                assertFalse(it is Resource.Error)
                assertFalse(it.data?.error?:false)
            }
        }
    }


}