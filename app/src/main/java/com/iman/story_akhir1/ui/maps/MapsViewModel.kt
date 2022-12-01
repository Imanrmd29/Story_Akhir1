package com.iman.story_akhir1.ui.maps

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.iman.story_akhir1.core.DataRepo

class MapsViewModel(private val dataRepository: DataRepo): ViewModel() {

    fun getStories() = dataRepository.getLocalStories().asLiveData()
}