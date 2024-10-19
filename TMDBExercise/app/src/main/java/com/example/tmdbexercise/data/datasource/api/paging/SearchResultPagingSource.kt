package com.example.tmdbexercise.data.datasource.api.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState

class SearchResultPagingSource<T : Any>(
    private val pagingSource: PagingSource<Int, T>, // This will now accept any type
    private val mapper: (T) -> SearchResult // Function to map from T to SearchResult
) : PagingSource<Int, SearchResult>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, SearchResult> {
        return try {
            val page = params.key ?: 1
            val response = pagingSource.load(params)

            when (response) {
                is LoadResult.Page -> {
                    // Transform the loaded items using the mapper
                    val results = response.data.map(mapper)

                    LoadResult.Page(
                        data = results,
                        prevKey = response.prevKey,
                        nextKey = response.nextKey
                    )
                }
                is LoadResult.Error -> {
                    LoadResult.Error(response.throwable)
                }
                is LoadResult.Invalid -> {
                    LoadResult.Invalid()
                }
            }
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, SearchResult>): Int? {
        return state.anchorPosition
    }
}