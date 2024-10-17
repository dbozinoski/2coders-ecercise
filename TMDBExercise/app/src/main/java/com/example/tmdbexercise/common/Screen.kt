package com.example.tmdbexercise.common

import kotlinx.serialization.Serializable

@Serializable
data class HomeScreen(
    val title: String
)

@Serializable
data class DetailsScreen(
    val movieId: Int
)