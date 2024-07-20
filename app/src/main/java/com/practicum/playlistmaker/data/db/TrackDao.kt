package com.practicum.playlistmaker.data.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface TrackDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(track: TrackEntity)

    @Query("DELETE FROM track WHERE trackId = :trackId")
    suspend fun delete(trackId: Int)

    @Query("DELETE FROM track WHERE trackId IN (:idList)")
    suspend fun delete(idList: List<Int>)

}