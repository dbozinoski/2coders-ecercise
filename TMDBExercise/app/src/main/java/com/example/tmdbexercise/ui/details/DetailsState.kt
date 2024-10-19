package com.example.tmdbexercise.ui.details

import com.example.tmdbexercise.data.model.Movie

sealed class DetailsState {
    object Loading : DetailsState()
    data class Success(
        val movie: Movie
    ) : DetailsState()

    data class Error(
        val message: String
    ) : DetailsState()
}
