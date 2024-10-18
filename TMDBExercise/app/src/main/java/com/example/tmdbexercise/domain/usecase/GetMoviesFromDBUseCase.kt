package com.example.tmdbexercise.domain.usecase

import com.example.tmdbexercise.data.model.Movie
import com.example.tmdbexercise.domain.repository.MovieRepository
import javax.inject.Inject

class GetMoviesFromDBUseCase @Inject constructor(
    private val moviesRepository: MovieRepository
) {

    suspend fun getMoviesFromDB(): List<Movie> {
        return moviesRepository.getMoviesFromDB()
    }
}