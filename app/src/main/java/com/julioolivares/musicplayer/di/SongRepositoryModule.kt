package com.julioolivares.musicplayer.di

import android.content.Context
import com.julioolivares.musicplayer.repository.SongsRepository
import com.julioolivares.musicplayer.repository.SongsRepositoryImpl
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val sonRepositoryModule = module {

    fun provideSongRepositoy(context: Context) : SongsRepository {
        return  SongsRepositoryImpl(context)
    }

    single {
        provideSongRepositoy(context = androidContext())
    }
}