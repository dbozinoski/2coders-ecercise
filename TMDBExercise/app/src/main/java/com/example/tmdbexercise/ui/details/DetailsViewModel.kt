package com.example.tmdbexercise.ui.details

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.example.tmdbexercise.common.DetailsScreen
import com.example.tmdbexercise.domain.usecase.GetMovieUseCase
import com.example.tmdbexercise.ui.home.HomeState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.getOrThrow

@HiltViewModel
class DetailsViewModel @Inject constructor(private val movieUseCase: GetMovieUseCase, savedStateHandle: SavedStateHandle) : ViewModel(){
    private val TAG = "HomeViewModel"

    private val _args = MutableStateFlow<DetailsScreen>(savedStateHandle.toRoute<DetailsScreen>())
    val args: StateFlow<DetailsScreen> = _args.asStateFlow()

    private val _state = MutableStateFlow<DetailsState>(DetailsState.Loading)
    val state: StateFlow<DetailsState> = _state.asStateFlow()

    init {
        getMovie()
    }

    private fun getMovie(){
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
}