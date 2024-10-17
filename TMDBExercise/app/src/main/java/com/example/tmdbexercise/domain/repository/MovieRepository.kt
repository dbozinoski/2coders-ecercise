package com.example.tmdbexercise.domain.repository

import androidx.paging.PagingSource
import com.example.tmdbexercise.data.model.Movie
import com.example.tmdbexercise.data.model.MovieList

interface MovieRepository {

    fun getMovies(): PagingSource<Int, Movie>

    suspend fun getMovie(movieId: Int): Result<Movie>?
}