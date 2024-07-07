package com.practicum.playlistmaker.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(version = 1, entities = [TrackEntity::class])
@TypeConverters(Converters::class)
abstract class AppDatabase: RoomDatabase() {

    abstract fun favoriteDao(): FavoriteDao

}