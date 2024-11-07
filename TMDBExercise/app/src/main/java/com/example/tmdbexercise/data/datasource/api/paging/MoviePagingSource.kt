package com.example.tmdbexercise.data.datasource.api.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.tmdbexercise.common.Constants
import com.example.tmdbexercise.data.datasource.api.ApiService
import com.example.tmdbexercise.data.model.Movie

class MoviePagingSource(
    private val apiService: ApiService
) : PagingSource<Int, Movie>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Movie> {
        return try {
            val currentPage = params.key ?: 1
            val response = apiService.getPopularMovies(Constants.API_KEY, page = currentPage)
            if (response.isSuccessful) {
                val responseBody = response.body()
                LoadResult.Page(
                    data = responseBody?.movies ?: listOf(),
                    prevKey = if (currentPage == 1) 0 else currentPage - 1,
                    nextKey = if (currentPage < (responseBody?.totalPages ?: 0)) currentPage + 1 else 0
                )
            } else {
                LoadResult.Error(Exception("Invalid response type"))
            }
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Movie>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }
}