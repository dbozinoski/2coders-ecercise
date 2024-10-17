package com.example.tmdbexercise.ui.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.tmdbexercise.data.model.Movie
import com.example.tmdbexercise.data.model.MovieList
import com.example.tmdbexercise.domain.usecase.GetMoviesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    moviesUseCase: GetMoviesUseCase
) : ViewModel() {
    private val TAG = "HomeViewModel"

    private val _state = MutableStateFlow<HomeState>(HomeState.Loading)
    val state: StateFlow<HomeState> = _state.asStateFlow()

    val movies: Flow<PagingData<Movie>> = moviesUseCase().cachedIn(viewModelScope)

//    init {
//        getMovies()
//    }

//    private fun getMovies() {
//        viewModelScope.launch {
//            try {
//                val result = moviesUseCase()
//                if(result != null) {
//                    if (result.isSuccess) {
//                        _state.value = HomeState.Success(result.getOrThrow())
//                    } else {
//                        _state.value =
//                            HomeState.Error(result.exceptionOrNull()?.message ?: "Unknown error")
//                    }
//                } else {
//                    HomeState.Error("Network issue")
//                }
//            } catch (e: Exception) {
//                Log.e(TAG, "Error: ${e.message}")
//                _state.value = HomeState.Error("Error: ${e.message}")
//            }
//        }
//    }
}