package com.example.tmdbexercise.data.repository

import androidx.paging.PagingSource
import com.example.tmdbexercise.common.Constants
import com.example.tmdbexercise.data.datasource.api.ApiService
import com.example.tmdbexercise.data.datasource.api.paging.MoviePagingSource
import com.example.tmdbexercise.data.datasource.api.exception.ApiResponseHandler
import com.example.tmdbexercise.data.model.Movie
import com.example.tmdbexercise.data.repository.datasource.FavouriteMovieLocalDataSource
import com.example.tmdbexercise.domain.repository.MovieRepository
import javax.inject.Inject

class MovieRepositoryImpl @Inject constructor(
    private val apiService: ApiService,
    private val favouriteMovieLocalDataSource: FavouriteMovieLocalDataSource
) : MovieRepository {
    private val TAG = "MovieRepositoryImpl"

    private val responseHandler = ApiResponseHandler()

//    override suspend fun getMovies(): Result<MovieList>? {
//        val response = apiService.getPopularMovies(Constants.API_KEY)
//        return responseHandler.handleResponse(response, TAG)
//    }

    override suspend fun getMovie(movieId: Int): Result<Movie>? {
        val  response = apiService.getMovie(movieId, Constants.API_KEY)
        return responseHandler.handleResponse(response, TAG)
    }

    override fun getMovies(): PagingSource<Int, Movie> {
        return MoviePagingSource(apiService)
    }

    override suspend fun saveMovieToDB(movie: Movie) {
        favouriteMovieLocalDataSource.saveMovieToDB(movie)
    }

    override suspend fun removeMovieFromDB(movieId: Int) {
        favouriteMovieLocalDataSource.removeMovieFromDB(movieId)
    }

    override suspend fun getMoviesFromDB(): List<Movie> {
        return favouriteMovieLocalDataSource.getMoviesFromDB()
    }


}