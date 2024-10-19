package com.example.tmdbexercise.ui.search

import com.example.tmdbexercise.data.model.MovieList

sealed class SearchState {
    object Initial : SearchState()
    object Loading : SearchState()
    data class Success(
        val data: MovieList
    ) : SearchState()
    data class Error(
        val message: String
    ) : SearchState()
}