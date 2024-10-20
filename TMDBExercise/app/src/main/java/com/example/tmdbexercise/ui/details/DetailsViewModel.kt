package com.example.tmdbexercise.ui.details

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.example.tmdbexercise.common.DetailsScreen
import com.example.tmdbexercise.data.model.Movie
import com.example.tmdbexercise.domain.usecase.GetMovieUseCase
import com.example.tmdbexercise.domain.usecase.GetMoviesFromDBUseCase
import com.example.tmdbexercise.domain.usecase.RemoveMovieFromDBUseCase
import com.example.tmdbexercise.domain.usecase.SaveMovieToDBUseCase
import com.example.tmdbexercise.ui.home.HomeState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.getOrThrow

@HiltViewModel
class DetailsViewModel @Inject constructor(
    private val movieUseCase: GetMovieUseCase,
    private val saveMovieToDBUseCase: SaveMovieToDBUseCase,
    private val getMoviesFromDBUseCase: GetMoviesFromDBUseCase,
    private val removeMovieFromDBUseCase: RemoveMovieFromDBUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel(){
    private val TAG = "HomeViewModel"

    // State to hold the args passed to the screen
    private val _args = MutableStateFlow<DetailsScreen>(savedStateHandle.toRoute<DetailsScreen>())
    val args: StateFlow<DetailsScreen> = _args.asStateFlow()

    // State to hold the current state
    private val _state = MutableStateFlow<DetailsState>(DetailsState.Loading)
    val state: StateFlow<DetailsState> = _state.asStateFlow()

    // State to hold isFavorite flag
    private val _isFavorite = MutableStateFlow<Boolean>(false)
    val isFavorite = _isFavorite.asStateFlow()

    init {
        getMovie()
    }

    fun getMovie(){
        viewModelScope.launch {
            try {
                val result = movieUseCase.execute(args.value.movieId)
                if(result != null) {
                    if (result.isSuccess) {
                        _state.value = DetailsState.Success(result.getOrThrow())
                    } else {
                        _state.value =
                            DetailsState.Error(result.exceptionOrNull()?.message ?: "Unknown error")
                    }
                } else {
                    HomeState.Error("Network error")
                }
            } catch (e: Exception) {
                Log.e(TAG, "Error: ${e.message}")
                _state.value = DetailsState.Error("Error: ${e.message}")
            }
        }

    }

    //Save Movie to DB
    fun saveMovie(movie: Movie){
        viewModelScope.launch{
            saveMovieToDBUseCase.saveMovieToDb(movie)
            _isFavorite.value = true
        }
    }

    //Remove Movie from DB
    fun removeMovieFromFavourites(movieId: Int){
        viewModelScope.launch{
            removeMovieFromDBUseCase.removeMovieFromDB(movieId)
            _isFavorite.value = false
        }
    }

    //Check if Movie is in DB
    fun isMovieFavorite(movieId: Int): Boolean{
        viewModelScope.launch{
            val favouriteMovies = getMoviesFromDBUseCase.getMoviesFromDB()
            for(movie in favouriteMovies){
                if(movie.id == movieId){
                    _isFavorite.value = true
                    true
                }
            }
        }
        return false
    }
}