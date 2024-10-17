package com.example.tmdbexercise.data.repository.datasource

import com.example.tmdbexercise.data.model.Movie

interface FavouriteMovieLocalDataSource {
    suspend fun getMoviesFromDB(): List<Movie>
    suspend fun saveMovieToDB(movie: Movie)
    suspend fun removeMovieFromDB(movieId: Int)
}