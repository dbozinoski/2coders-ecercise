package com.example.tmdbexercise.ui.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.tmdbexercise.data.datasource.api.paging.SearchResult
import com.example.tmdbexercise.domain.usecase.SearchMoviesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flatMapLatest
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    searchMoviesUseCase: SearchMoviesUseCase
) : ViewModel() {
    private val TAG = "SearchViewModel"

    // State to hold the current state
    private val _state = MutableStateFlow<SearchState>(SearchState.Initial)
    val state: StateFlow<SearchState> = _state.asStateFlow()

    // State to hold the current query
    private val _query = MutableStateFlow<String>("")
    // State to hold the current search type
    private val _searchType = MutableStateFlow<SearchType>(SearchType.Movies) // Default to Movies

    @OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
    val results: Flow<PagingData<SearchResult>> = combine(_query, _searchType) { query, searchType ->
        Pair(query, searchType)
    }.debounce(300) // Debounce to avoid searching too frequently on rapid input
        .filter { (query, _) -> query.isNotEmpty() } // Only trigger searches for non-empty queries
        .flatMapLatest { (query, searchType) ->
            searchMoviesUseCase(keyWord = query, searchType = searchType) // Call the use case with the query and search type
                .cachedIn(viewModelScope) // Cache results in the ViewModel scope
        }

    fun searchMovies(query: String) {
        _query.value = query // Update the query state
    }

    fun setSearchType(searchType: SearchType) {
        _searchType.value = searchType // Update the search type state
    }

    // Enum class for search types
    enum class SearchType {
        Movies,
        TV
    }
}