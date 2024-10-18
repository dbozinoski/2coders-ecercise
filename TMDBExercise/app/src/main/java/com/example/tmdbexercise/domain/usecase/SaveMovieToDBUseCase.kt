package com.example.tmdbexercise.domain.usecase

import com.example.tmdbexercise.data.model.Movie
import com.example.tmdbexercise.domain.repository.MovieRepository
import javax.inject.Inject

class SaveMovieToDBUseCase @Inject constructor(
    private val moviesRepository: MovieRepository
) {
    suspend fun saveMovieToDb(movie: Movie) {
        moviesRepository.saveMovieToDB(movie)
    }
}