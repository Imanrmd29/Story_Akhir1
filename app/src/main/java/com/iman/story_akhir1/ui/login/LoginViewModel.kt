package com.iman.story_akhir1.ui.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.iman.story_akhir1.core.DataRepo

class LoginViewModel (private val dataRepository: DataRepo) : ViewModel() {
    private var email = ""
    private var password = ""

    fun setLoginParam(email: String, password: String) {
        this.email = email
        this.password = password
    }

    fun doLogin() = dataRepository.doLogin(email, password).asLiveData()
}