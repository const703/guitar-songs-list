package com.konst.guitarsongslist.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(version = 1, entities = [Song::class])
abstract class AppDatabase : RoomDatabase() {
    abstract val songDao: SongDao

    companion object {
        private var instance: AppDatabase? = null
        operator fun invoke(context: Context): AppDatabase {
            return instance ?: synchronized(this) {
                instance ?: Room.databaseBuilder(
                    context, AppDatabase::class.java, "database.db"
                ).build().also { instance = it }
            }
        }
    }
}