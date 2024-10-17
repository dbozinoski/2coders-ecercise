package com.example.tmdbexercise.data.datasource.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.tmdbexercise.data.model.Movie

@Dao
interface MovieDao {


    @Query("SELECT * FROM popular_movies")
    suspend fun getMovies(): List<Movie>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveMovie(movie : Movie)

    @Query("DELETE FROM popular_movies WHERE id = :movieId")
    suspend fun deleteMovie(movieId: Int)
}