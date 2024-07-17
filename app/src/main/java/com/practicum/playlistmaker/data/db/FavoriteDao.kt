package com.practicum.playlistmaker.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface FavoriteDao {

    @Query("SELECT * FROM favorite ORDER BY addDate DESC")
    suspend fun getAll(): List<FavoriteEntity>

    @Query("SELECT trackId FROM favorite WHERE trackId = :trackId")
    suspend fun getOne(trackId: Int): List<Int>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(track: FavoriteEntity)

    @Query("DELETE FROM favorite WHERE trackId = :trackId")
    suspend fun delete(trackId: Int)

}