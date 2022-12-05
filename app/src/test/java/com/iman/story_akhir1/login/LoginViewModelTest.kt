package com.iman.story_akhir1.login

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.iman.story_akhir1.DataDummy
import com.iman.story_akhir1.MainDispatcherRule
import com.iman.story_akhir1.core.DataRepo
import com.iman.story_akhir1.core.Resource
import com.iman.story_akhir1.core.data.remote.model.LoginRespon
import com.iman.story_akhir1.ui.login.LoginViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runTest
import org.junit.Assert
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
class LoginViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var dataRepository: DataRepo
    private lateinit var loginViewModel: LoginViewModel
    private val loginResponseMock = DataDummy.loginResponse()
    private val emailMock = "iman@email.com"
    private val passwordMock = "12345678"

    @Before
    fun setUp() {
        loginViewModel = LoginViewModel(dataRepository)
        loginViewModel.setLoginParam(emailMock, passwordMock)
    }

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Test
    fun `when login success`()  = runTest {
        val expectedResponse : Flow<Resource<LoginRespon>> = flow {
            emit(Resource.Success(loginResponseMock))
        }
        `when`(dataRepository.doLogin(emailMock, passwordMock)).thenReturn(
            expectedResponse
        )
        loginViewModel.doLogin().observeForever {
            verify(dataRepository).doLogin(emailMock, passwordMock)
            Assert.assertNotNull(it.data)
            Assert.assertTrue(it is Resource.Success)
            Assert.assertFalse(it is Resource.Error)
            Assert.assertFalse(it.data?.error?:false)
        }
    }

    @Test
    fun `when login failed`() = runTest {
        val expectedResponse : Flow<Resource<LoginRespon>> = flow {
            emit(Resource.Error("", null))
        }
        `when`(dataRepository.doLogin(emailMock, passwordMock)).thenReturn(
            expectedResponse
        )
        loginViewModel.doLogin().observeForever {
            verify(dataRepository).doLogin(emailMock, passwordMock)
            Assert.assertNull(it.data)
            Assert.assertFalse(it is Resource.Success)
            Assert.assertTrue(it is Resource.Error)
            Assert.assertNotNull(it.message)
        }
    }
}