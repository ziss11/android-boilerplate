package com.ziss.notesappandroid

import android.app.Application
import com.ziss.notesappandroid.core.coreModule
import com.ziss.notesappandroid.core.databaseModule
import com.ziss.notesappandroid.core.networkModule
import com.ziss.notesappandroid.modules.auth.authModule
import com.ziss.notesappandroid.modules.home.homeModule
import com.ziss.notesappandroid.modules.note.noteModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

open class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger(Level.NONE)
            androidContext(this@MyApplication)
            modules(
                listOf(
                    coreModule,
                    databaseModule,
                    networkModule,
                    authModule,
                    homeModule,
                    noteModule,
                )
            )
        }
    }
}