package com.example.tmdbexercise.ui.home

import com.example.tmdbexercise.data.model.MovieList

sealed class HomeState {
    object Loading : HomeState()
    data class Success(
        val data: MovieList
    ) : HomeState()
    data class Error(
        val message: String
    ) : HomeState()
}
