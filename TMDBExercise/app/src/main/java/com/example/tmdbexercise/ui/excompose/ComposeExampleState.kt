package com.example.tmdbexercise.ui.excompose

import com.example.tmdbexercise.data.model.MovieList

sealed class ComposeExampleState {
    object Loading : ComposeExampleState()
    data class Success(
        val data: MovieList
    ) : ComposeExampleState()
    data class Error(
        val message: String
    ) : ComposeExampleState()
}