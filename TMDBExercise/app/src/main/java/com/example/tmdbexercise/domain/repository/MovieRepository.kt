package com.example.tmdbexercise.domain.repository

import androidx.paging.PagingSource
import com.example.tmdbexercise.data.model.Movie
import com.example.tmdbexercise.data.model.TV

interface MovieRepository {

    fun getMovies(): PagingSource<Int, Movie>
    suspend fun getMovie(movieId: Int): Result<Movie>?
    suspend fun saveMovieToDB(movie: Movie)
    suspend fun removeMovieFromDB(movieId: Int)
    suspend fun getMoviesFromDB(): List<Movie>
    fun searchMovie(keyWord: String): PagingSource<Int, Movie>
    fun searchTv(keyWord: String): PagingSource<Int, TV>
}