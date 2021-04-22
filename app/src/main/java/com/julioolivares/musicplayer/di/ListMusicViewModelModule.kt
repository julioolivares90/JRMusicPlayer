package com.julioolivares.musicplayer.di

import com.julioolivares.musicplayer.viewModels.ListMusicViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

val listMusicViewModelModule = module {
    viewModel {

        ListMusicViewModel(context = androidContext())
    }

}