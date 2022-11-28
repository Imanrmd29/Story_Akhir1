package com.iman.story_akhir1.ui.maps

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.story.app.core.DataRepository

class MapsViewModel(private val dataRepository: DataRepository): ViewModel() {

    fun getStories() = dataRepository.getLocalStories().asLiveData()
}