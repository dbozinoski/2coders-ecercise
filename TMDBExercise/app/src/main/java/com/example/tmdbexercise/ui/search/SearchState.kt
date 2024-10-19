package com.example.tmdbexercise.ui.search

import com.example.tmdbexercise.data.model.MovieList
import com.example.tmdbexercise.ui.home.HomeState

sealed class SearchState {
    object Loading : SearchState()
    object Initial : SearchState()
    data class Success(
        val data: MovieList
    ) : SearchState()
    data class Error(
        val message: String
    ) : SearchState()}