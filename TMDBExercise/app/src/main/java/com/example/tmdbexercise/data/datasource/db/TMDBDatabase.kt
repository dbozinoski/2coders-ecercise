package com.example.tmdbexercise.data.datasource.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.tmdbexercise.data.datasource.db.dao.MovieDao
import com.example.tmdbexercise.data.model.Movie

@Database(
    entities = [Movie::class],
    version = 1,
    exportSchema = false
)
abstract class TMDBDatabase : RoomDatabase() {

    abstract fun movieDao() : MovieDao
}