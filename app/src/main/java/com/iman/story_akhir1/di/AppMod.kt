package com.iman.story_akhir1.di

import com.iman.story_akhir1.ui.add.AddViewModel
import com.iman.story_akhir1.ui.home.HomeViewModel
import com.iman.story_akhir1.ui.login.LoginViewModel
import com.iman.story_akhir1.ui.maps.MapsViewModel
import com.iman.story_akhir1.ui.register.RegisterViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelMod = module {
    viewModel { LoginViewModel(get()) }
    viewModel { RegisterViewModel(get()) }
    viewModel { HomeViewModel(get()) }
    viewModel { AddViewModel(get()) }
    viewModel { MapsViewModel(get()) }
}