package com.julioolivares.musicplayer

import android.app.Application
import com.julioolivares.musicplayer.di.listMusicViewModelModule
import com.julioolivares.musicplayer.di.sonRepositoryModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class MusicApp : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger()
            androidContext(this@MusicApp)
            modules(
                listMusicViewModelModule,
                sonRepositoryModule
            )
        }
    }
}