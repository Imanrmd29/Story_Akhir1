package com.iman.story_akhir1.core.data.local

import com.iman.story_akhir1.core.data.local.room.StoryDao

class LocalDatasource (
    private val storyDao: StoryDao
) {
    fun getStories() = storyDao.getAllStories()
}