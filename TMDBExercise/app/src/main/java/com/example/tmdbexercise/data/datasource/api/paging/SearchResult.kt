package com.example.tmdbexercise.data.datasource.api.paging

import com.example.tmdbexercise.data.model.Movie
import com.example.tmdbexercise.data.model.TV

sealed class SearchResult {
    data class MovieResult(val movie: Movie) : SearchResult()
    data class TVResult(val tv: TV) : SearchResult()
}