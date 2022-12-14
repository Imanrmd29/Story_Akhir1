package com.iman.story_akhir1.ui.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.iman.story_akhir1.core.DataRepo

class RegisterViewModel(private val dataRepository: DataRepo) : ViewModel() {
    private var name = ""
    private var email = ""
    private var password = ""

    fun setRegisterParam(name: String, email: String, password: String) {
        this.name = name
        this.email = email
        this.password = password
    }

    fun doRegister() = dataRepository.doRegister(email, password, name).asLiveData()
}