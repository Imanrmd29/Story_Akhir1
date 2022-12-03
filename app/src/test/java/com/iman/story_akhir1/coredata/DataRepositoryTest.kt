package com.iman.story_akhir1.coredata

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.paging.AsyncPagingDataDiffer
import androidx.paging.PagingData
import androidx.recyclerview.widget.ListUpdateCallback
import com.iman.story_akhir1.com.SharedPreferenceProvider
import com.iman.story_akhir1.core.DataRepo
import com.iman.story_akhir1.core.data.local.LocalDatasource
import com.iman.story_akhir1.core.data.paging.StoryPagingSource
import com.iman.story_akhir1.core.data.remote.RemoteDatasource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.advanceUntilIdle
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
import com.iman.story_akhir1.DataDummy
import com.iman.story_akhir1.MainDispatcherRule
import com.iman.story_akhir1.PageDataSourceTest
import com.iman.story_akhir1.core.Resource
import com.iman.story_akhir1.core.data.local.entity.StoryEntity
import com.iman.story_akhir1.core.data.remote.model.GeneralRespon
import com.iman.story_akhir1.core.data.remote.model.LoginRespon
import com.iman.story_akhir1.core.data.remote.network.ApiRespon
import com.iman.story_akhir1.ui.home.HomeAdapter

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
    private val tokenMock = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VySWQiOiJ1c2VyLXRrenRtVi1tQ0hHRDYyOEQiLCJpYXQiOjE2NjcxMTcyMTZ9.sR3fwAxHj64b5aGa7KfuhlGaQzw-zUx2hn8t_kjt0W0"
    private val nameMock = "bukan iman"
    private val loginResponseMock = DataDummy.loginResponse()
    private val generalResponseMock = DataDummy.generalResponse()
    private val storyEntityMock = DataDummy.getStories()
    private val fileMock = DataDummy.multipartFile()
    private val descriptionMock = "bukan deskripsi"
    private val latitudeMock = -7.7856193F
    private val longitudeMock = 110.46422F

    @Before
    fun setup(){
        dataRepository = DataRepo(remoteDatasource, sharedPreferenceProvider, storyPagingSource, localDatasource)
    }

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Test
    fun `when login`()  = runTest {
        val expectedResponse : Flow<ApiRespon<LoginRespon>> = flow {
            emit(ApiRespon.Success(loginResponseMock))
        }
        `when`(remoteDatasource.doLogin(emailMock, passwordMock)).thenReturn(
            expectedResponse
        )
        dataRepository.doLogin(emailMock, passwordMock).collect {
            if (it !is Resource.Loading){
                verify(remoteDatasource).doLogin(emailMock, passwordMock)
                assertNotNull(it.data)
                assertTrue(it is Resource.Success)
                assertFalse(it is Resource.Error)
                assertFalse(it.data?.error?:false)
            }
        }
    }

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

    @Test
    fun `when get stories`()  = runTest {
        val data = PageDataSourceTest.snapshot(storyEntityMock)
        val expectedResponse : Flow<PagingData<StoryEntity>> = flow {
            emit(data)
        }

        `when`(storyPagingSource.getStories(tokenMock)).thenReturn(
            expectedResponse
        )
        dataRepository.getStories(tokenMock).collect {
            val differ = AsyncPagingDataDiffer(
                diffCallback = HomeAdapter.DIFF_CALLBACK,
                updateCallback = noopListUpdateCallback,
                mainDispatcher = mainDispatcherRule.testDispatcher,
                workerDispatcher = mainDispatcherRule.testDispatcher
            )
            CoroutineScope(Dispatchers.IO).launch {
                differ.submitData(it)
                advanceUntilIdle()
                verify(storyPagingSource).getStories(tokenMock)
                assertNotNull(differ.snapshot())
                assertEquals(differ.snapshot().size, storyEntityMock.size)
            }

        }
    }

    @Test
    fun `when get stories from local`()  = runTest {
        val expectedResponse : Flow<List<StoryEntity>> = flow {
            emit(storyEntityMock)
        }
        `when`(localDatasource.getStories()).thenReturn(
            expectedResponse
        )
        dataRepository.getLocalStories().collect {
            verify(localDatasource).getStories()
            assertNotNull(it)
            assertTrue(it.isNotEmpty())
            assertEquals(it.size, storyEntityMock.size)
        }
    }

    @Test
    fun `when add story`()  = runTest {
        val expectedResponse : Flow<ApiRespon<GeneralRespon>> = flow {
            emit(ApiRespon.Success(generalResponseMock))
        }
        `when`(remoteDatasource.addNewStory(tokenMock, fileMock, descriptionMock, latitudeMock, longitudeMock)).thenReturn(
            expectedResponse
        )
        dataRepository.addNewStory(tokenMock, fileMock, descriptionMock, latitudeMock, longitudeMock).collect {
            if (it !is Resource.Loading){
                verify(remoteDatasource).addNewStory(tokenMock, fileMock, descriptionMock, latitudeMock, longitudeMock)
                assertNotNull(it.data)
                assertTrue(it is Resource.Success)
                assertFalse(it is Resource.Error)
                assertFalse(it.data?.error?:false)
            }
        }
    }


    private val noopListUpdateCallback = object : ListUpdateCallback {
        override fun onInserted(position: Int, count: Int) {}
        override fun onRemoved(position: Int, count: Int) {}
        override fun onMoved(fromPosition: Int, toPosition: Int) {}
        override fun onChanged(position: Int, count: Int, payload: Any?) {}
    }
}