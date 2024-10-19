package com.example.tmdbexercise.data.datasource.api

import com.example.tmdbexercise.data.model.Movie
import com.example.tmdbexercise.data.model.MovieList
import com.example.tmdbexercise.data.model.TV
import com.example.tmdbexercise.data.model.TvList
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    @GET("movie/popular")
    suspend fun getPopularMovies(
        @Query("api_key") apiKey: String,
        @Query("page") page: Int
    ): Response<MovieList>

    @GET("movie/{movie_id}")
    suspend fun getMovie(
        @Path("movie_id") movieId: Int,
        @Query("api_key") apiKey: String,
    ): Response<Movie>

    @GET("search/movie")
    suspend fun searchMovie(
        @Query("query") text: String,
        @Query("api_key") apiKey: String,
        @Query("page") page: Int
    ): Response<MovieList>

    @GET("search/tv")
    suspend fun searchTv(
        @Query("query") text: String,
        @Query("api_key") apiKey: String,
        @Query("page") page: Int
    ): Response<TvList>
}