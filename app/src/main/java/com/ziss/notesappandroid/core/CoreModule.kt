package com.ziss.notesappandroid.core

import androidx.room.Room
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys
import com.ziss.notesappandroid.core.common.TokenManager
import com.ziss.notesappandroid.core.interceptors.ErrorInterceptor
import com.ziss.notesappandroid.core.interceptors.TokenInterceptor
import com.ziss.notesappandroid.core.services.AppDatabase
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import java.util.concurrent.TimeUnit


val coreModule = module {
    single { TokenManager() }
}

val databaseModule = module {
    single {
        Room.databaseBuilder(
            androidContext(),
            AppDatabase::class.java, "note.db"
        ).fallbackToDestructiveMigration(false).build()
    }
    single {
        val masterKeyAlias = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC)
        EncryptedSharedPreferences.create(
            "secret_shared_prefs",
            masterKeyAlias,
            get(),
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )
    }
    single { get<AppDatabase>().noteDao() }
    single { get<AppDatabase>().userDao() }
}

val networkModule = module {
    single {
        val loggingInterceptor =
            HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
        val tokenInterceptor = TokenInterceptor(get())
        val errorInterceptor = ErrorInterceptor()

        OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .addInterceptor(tokenInterceptor)
            .addInterceptor(errorInterceptor)
            .connectTimeout(120, TimeUnit.SECONDS)
            .readTimeout(120, TimeUnit.SECONDS)
            .build()
    }
}