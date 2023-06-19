package com.konst.guitarsongslist.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert

@Dao
interface SongDao {
    @Upsert
    suspend fun upsertSong(song: Song): Long

    @Delete
    suspend fun deleteSong(song: Song)

    @Query("DELETE FROM song WHERE id = :songId")
    suspend fun deleteSongById(songId: Long)

    @Query("SELECT song.id, artist_name, song_name, last_played_time FROM song;")
    suspend fun getAllSongs(): List<Song>
}