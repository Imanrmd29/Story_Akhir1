package com.iman.story_akhir1.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.iman.story_akhir1.core.DataRepo

class HomeViewModel(private val dataRepository: DataRepo) : ViewModel() {

    fun getStories(token: String) = dataRepository.getStories(token).asLiveData()

}