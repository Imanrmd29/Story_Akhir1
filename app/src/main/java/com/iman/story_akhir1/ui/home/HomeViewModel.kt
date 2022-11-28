package com.iman.story_akhir1.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.story.app.core.DataRepository

class HomeViewModel(private val dataRepository: DataRepository) : ViewModel() {

    fun getStories(token: String) = dataRepository.getStories(token).asLiveData()

}