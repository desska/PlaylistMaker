package com.practicum.playlistmaker.di

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.media.MediaPlayer
import androidx.room.Room
import com.google.gson.Gson
import com.practicum.playlistmaker.data.db.AppDatabase
import com.practicum.playlistmaker.newlist.data.impl.FileServiceImpl
import com.practicum.playlistmaker.newlist.domain.FileService
import com.practicum.playlistmaker.search.data.entity.ItunesService
import com.practicum.playlistmaker.search.data.impl.HistoryLocalStorageImpl
import com.practicum.playlistmaker.search.domain.HistoryLocalStorage
import com.practicum.playlistmaker.sharing.data.impl.ExternalNavigatorImpl
import com.practicum.playlistmaker.sharing.domain.ExternalNavigator
import org.koin.android.ext.koin.androidContext
import org.koin.core.qualifier.named
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val historyQualifier = named("history")
val themeQualifier = named("theme")
val dataModule = module {
    single<ItunesService> {
        Retrofit.Builder().baseUrl("https://itunes.apple.com")
            .addConverterFactory(GsonConverterFactory.create()).build()
            .create(ItunesService::class.java)
    }

    single<HistoryLocalStorage> {
        HistoryLocalStorageImpl(get(historyQualifier), get())
    }

    single<SharedPreferences>(historyQualifier) {
        androidContext().getSharedPreferences("local_storage", Context.MODE_PRIVATE)
    }

    single<SharedPreferences>(themeQualifier) {
        androidContext().getSharedPreferences(
            "COMMON_PREFERENCE", Application.MODE_PRIVATE
        )
    }

    single<ExternalNavigator> {
        ExternalNavigatorImpl(androidContext())
    }

    single {
        MediaPlayer()
    }

    single {
        Room.databaseBuilder(androidContext(), AppDatabase::class.java, "database.db")
            .fallbackToDestructiveMigration()
            .build()
    }

    factory { Gson() }

    single<FileService> {
        FileServiceImpl(androidContext())
    }
}
