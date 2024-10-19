package com.example.tmdbexercise.domain.usecase

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.tmdbexercise.data.model.Movie
import com.example.tmdbexercise.domain.repository.MovieRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetMoviesUseCase @Inject constructor(
    private val moviesRepository: MovieRepository
) {
    operator fun invoke(pageSize: Int = 30): Flow<PagingData<Movie>> {
        return Pager(
            config = PagingConfig(
                prefetchDistance = 2,
                pageSize = pageSize,
                enablePlaceholders = false,
                maxSize = pageSize * 3
            ),
            pagingSourceFactory = { moviesRepository.getMovies() }
        ).flow
    }

}