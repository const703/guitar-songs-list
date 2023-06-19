package com.konst.guitarsongslist.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Song(
    @ColumnInfo(name = "song_name") val songName: String,
    @ColumnInfo(name = "artist_name") val artistName: String,
    @ColumnInfo(name = "last_played_time") val lastPlayedEpochSeconds: Long,
    @PrimaryKey(autoGenerate = true) val id: Long = 0
)
