package com.example.tmdbexercise.domain.usecase

import com.example.tmdbexercise.domain.repository.MovieRepository
import javax.inject.Inject

class RemoveMovieFromDBUseCase @Inject constructor(
    private val moviesRepository: MovieRepository
) {

    suspend fun removeMovieFromDB(movieId: Int) {
        moviesRepository.removeMovieFromDB(movieId)
    }
}