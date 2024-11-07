package com.example.tmdbexercise.ui.excompose

import androidx.lifecycle.ViewModel
import androidx.paging.PagingData
import com.example.tmdbexercise.data.model.Movie
import com.example.tmdbexercise.domain.usecase.GetMoviesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class ComposeExampleViewModel @Inject constructor(
    moviesUseCase: GetMoviesUseCase
) : ViewModel() {
    private val TAG = "ComposeExampleViewModel"

    // State to hold the current state
    private val _state = MutableStateFlow<ComposeExampleState>(ComposeExampleState.Loading)
    val state: StateFlow<ComposeExampleState> = _state.asStateFlow()

    val movies: Flow<PagingData<Movie>> = moviesUseCase.invoke()

}