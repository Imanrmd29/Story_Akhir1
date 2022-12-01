package com.iman.story_akhir1.core.data.local.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.iman.story_akhir1.core.data.local.entity.RemoteKeys
import com.iman.story_akhir1.core.data.local.entity.StoryEntity

@Database(
    entities = [StoryEntity::class, RemoteKeys::class],
    version = 1,
    exportSchema = false
)
abstract class StoryDatabase : RoomDatabase(){
    abstract fun dao(): StoryDao
}