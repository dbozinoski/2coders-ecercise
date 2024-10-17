package com.example.tmdbexercise.data.repository.datasourceImpl

import com.example.tmdbexercise.data.datasource.db.dao.MovieDao
import com.example.tmdbexercise.data.model.Movie
import com.example.tmdbexercise.data.repository.datasource.FavouriteMovieLocalDataSource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class FavouriteMovieLocalDataSourceImpl @Inject constructor(private val movieDao: MovieDao) :
    FavouriteMovieLocalDataSource {

    override suspend fun getMoviesFromDB(): List<Movie> {
        return movieDao.getMovies()
    }

    override suspend fun saveMovieToDB(movie: Movie) {
        CoroutineScope(Dispatchers.IO).launch {
            movieDao.saveMovie(movie)
        }
    }

    override suspend fun removeMovieFromDB(movieId: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            movieDao.deleteMovie(movieId)
        }
    }

}