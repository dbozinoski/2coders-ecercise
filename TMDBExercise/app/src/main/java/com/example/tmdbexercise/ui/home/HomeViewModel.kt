package com.example.tmdbexercise.ui.home

import androidx.lifecycle.ViewModel
import androidx.paging.PagingData
import androidx.paging.map
import com.example.tmdbexercise.data.model.Movie
import com.example.tmdbexercise.domain.usecase.GetMoviesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    moviesUseCase: GetMoviesUseCase
) : ViewModel() {
    private val TAG = "HomeViewModel"

    // State to hold the current state
    private val _state = MutableStateFlow<HomeState>(HomeState.Loading)
    val state: StateFlow<HomeState> = _state.asStateFlow()

    val movies: Flow<PagingData<Movie>> = moviesUseCase.invoke()

}