package com.example.tmdbexercise.domain.usecase

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.tmdbexercise.data.datasource.api.paging.SearchResult
import com.example.tmdbexercise.data.datasource.api.paging.SearchResultPagingSource
import com.example.tmdbexercise.domain.repository.MovieRepository
import com.example.tmdbexercise.ui.search.SearchViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SearchMoviesUseCase @Inject constructor(
    private val moviesRepository: MovieRepository
) {
    operator fun invoke(
        pageSize: Int = 30,
        keyWord: String,
        searchType: SearchViewModel.SearchType
    ): Flow<PagingData<SearchResult>> {
        return Pager(
            config = PagingConfig(
                prefetchDistance = 2,
                pageSize = pageSize,
                enablePlaceholders = false,
                maxSize = pageSize * 3
            ),
            pagingSourceFactory = {
                when (searchType) {
                    SearchViewModel.SearchType.Movies -> {
                        SearchResultPagingSource(
                            pagingSource = moviesRepository.searchMovie(keyWord),
                            mapper = { movie -> SearchResult.MovieResult(movie) } // Map Movie to SearchResult
                        )
                    }
                    SearchViewModel.SearchType.TV -> {
                        SearchResultPagingSource(
                            pagingSource = moviesRepository.searchTv(keyWord),
                            mapper = { tv -> SearchResult.TVResult(tv) } // Map TV to SearchResult
                        )
                    }
                }
            }
        ).flow
    }
}