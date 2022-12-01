package com.iman.story_akhir1

import android.app.Application
import com.iman.story_akhir1.core.di.databaseModule
import com.iman.story_akhir1.core.di.networkModule
import com.iman.story_akhir1.core.di.repositoryModule
import com.iman.story_akhir1.di.viewModelMod
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class MyApp: Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger()
            androidContext(this@MyApp)
            modules(
                listOf(
                    networkModule,
                    repositoryModule,
                    viewModelMod,
                    databaseModule
                )
            )
        }
    }
}