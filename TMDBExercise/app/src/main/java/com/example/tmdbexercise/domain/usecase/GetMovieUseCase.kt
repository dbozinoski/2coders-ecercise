package com.example.tmdbexercise.domain.usecase

import com.example.tmdbexercise.data.model.Movie
import com.example.tmdbexercise.domain.repository.MovieRepository
import javax.inject.Inject

class GetMovieUseCase @Inject constructor(
    private val moviesRepository: MovieRepository
) {
    suspend fun execute(movieId: Int): Result<Movie>? {

        val result = moviesRepository.getMovie(movieId)

        result?.fold(
            onSuccess = { response ->
                response
            },
            onFailure = { exception ->
            }
        )

        return result
    }

}