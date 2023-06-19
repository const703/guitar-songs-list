package com.konst.guitarsongslist.data

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    version = 1,
    entities = [Song::class]
)
abstract class AppDatabase : RoomDatabase() {
    abstract val songDao: SongDao
}