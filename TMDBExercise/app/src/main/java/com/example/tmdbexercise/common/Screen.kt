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

@Serializable
data class SearchScreen(
    val title: String
)

@Serializable
data class ComposeExampleScreen(
    val title: String
)