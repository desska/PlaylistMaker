package com.practicum.playlistmaker.data.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.practicum.playlistmaker.newlist.domain.entity.Playlist
import java.util.Date

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
    suspend fun getTracksId(playlistId: Int): List<Int>

    @Query("INSERT INTO track_playlist (playlistId, trackId, date) VALUES (:playlistId, :trackId, :date)")
    suspend fun addTrack(trackId: Int, playlistId: Int, date: Date = Date())

    @Insert(entity = PlaylistEntity::class, onConflict = OnConflictStrategy.REPLACE)
    suspend fun addList(playlist: PlaylistEntity)

    @Query("SELECT * FROM track_playlist INNER JOIN track ON track_playlist.trackId = track.trackId WHERE playlistId = :playlistId ORDER BY date DESC")
    suspend fun getTracks(playlistId: Int): List<TrackEntity>

    @Query("SELECT playlistId FROM track_playlist WHERE playlistId <> :playlistId AND trackId = :trackId ")
    suspend fun getAnotherPlaylistId(playlistId: Int, trackId: Int): List<Int>

    @Query("DELETE FROM track_playlist WHERE playlistId = :playlistId")
    suspend fun deleteTracksFromList(playlistId: Int)

    @Query("DELETE FROM track_playlist WHERE playlistId = :playlistId AND trackId = :trackId")
    suspend fun deleteTrackFromList(playlistId: Int, trackId: Int)

    @Query("DELETE FROM playlist WHERE id = :playlistId")
    suspend fun deleteList(playlistId: Int)

    @Query("SELECT * FROM playlist WHERE id = :playlistId")
    suspend fun getList(playlistId: Int): PlaylistEntity
    @Query(
        "WITH TracksInList AS (SELECT TrackId FROM  track_playlist WHERE playlistId = :playlistId), " +
                "TracksInAnotherList AS (SELECT DISTINCT track_playlist.TrackId FROM track_playlist INNER JOIN TracksInList ON  track_playlist.TrackId = TracksInList.TrackId   WHERE playlistId <> :playlistId) " +
                "SELECT TracksInList.trackId FROM TracksInList  LEFT JOIN TracksInAnotherList  ON TracksInList.TrackId =  TracksInAnotherList.TrackId WHERE TracksInAnotherList.TrackId IS NULL"
    )
    suspend fun getTracksNotExistsInLists(playlistId: Int): List<Int>
}