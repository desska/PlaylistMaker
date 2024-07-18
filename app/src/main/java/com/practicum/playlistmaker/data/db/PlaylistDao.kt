package com.practicum.playlistmaker.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface PlaylistDao {
    @Query(
        "WITH " +
                "TrackCount AS (" +
                " SELECT COUNT(*) AS quantity, " +
                " playlistId  FROM track_playlist GROUP BY playlistId" +
                ") " +
                "SELECT  id, name, description, cover , " +
                "CASE WHEN TrackCount.quantity IS NULL THEN 0 ELSE TrackCount.quantity END AS quantity " +
                "FROM playlist LEFT JOIN TrackCount ON playlist.id = TrackCount.playlistId"
    )
    suspend fun getLists(): List<PlaylistEntity>

    @Query("SELECT trackId FROM track_playlist WHERE playlistId = :playlistId")
    suspend fun getTracks(playlistId: Int): List<Int>

    @Query("INSERT INTO track_playlist (playlistId, trackId) VALUES (:playlistId, :trackId)")
    suspend fun addTrack(trackId: Int, playlistId: Int)

    @Insert(entity = PlaylistEntity::class, onConflict = OnConflictStrategy.REPLACE)
    suspend fun addList(playlist: PlaylistEntity)

}