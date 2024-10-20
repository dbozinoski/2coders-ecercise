package com.example.tmdbexercise.ui.search

import androidx.paging.AsyncPagingDataDiffer
import androidx.paging.PagingData
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListUpdateCallback
import app.cash.turbine.test
import com.example.tmdbexercise.data.datasource.api.paging.SearchResult
import com.example.tmdbexercise.data.model.Movie
import com.example.tmdbexercise.data.model.TV
import com.example.tmdbexercise.domain.usecase.SearchMoviesUseCase
import io.mockk.every
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class SearchViewModelTest {

    // A test dispatcher to run coroutines in a controlled way
    private val testDispatcher = UnconfinedTestDispatcher()

    // A sample list of movies
    val movieTvList = listOf(
        SearchResult.MovieResult(Movie(1, "Overview 1", "PosterPath 1", "Title 1", "ReleaseDate 1", 8.5, 100)),
        SearchResult.MovieResult(Movie(2, "Overview 2", "PosterPath 2", "Title 2", "ReleaseDate 2", 7.9, 200)),
        SearchResult.TVResult(TV(3, "Overview 3", "PosterPath 3", "Title 3", "ReleaseDate 3")),
        SearchResult.TVResult(TV(4, "Overview 3", "PosterPath 3", "Title 3", "ReleaseDate 3"))
    )

    // Converting the list to PagingData using PagingData.from()
    val mockPagingData = PagingData.from(movieTvList)

    // Mocking the Flow<PagingData<Movie>> to return the mockPagingData
    val mockFlow: Flow<PagingData<SearchResult>> = flowOf(mockPagingData)

    private val mockUseCaseMovie = mockk<SearchMoviesUseCase>(relaxed = true) {
        every { this@mockk.invoke(keyWord = any(), searchType = any()) } returns mockFlow
    }

    // The ViewModel under test
    private lateinit var searchViewModel: SearchViewModel
    @Before
    fun setup() {
        // Set the test dispatcher as the main dispatcher for coroutine contexts
        Dispatchers.setMain(testDispatcher)
        // Initialize the ViewModel with the mocked use case
        searchViewModel = SearchViewModel(mockUseCaseMovie)
    }

    @After
    fun tearDown() {
        // Reset the main dispatcher to its original state
        Dispatchers.resetMain()
    }

    @Test
    fun `results flow emits paging data when query is provided`() = runTest {
        searchViewModel.searchMovies("o")

        // Use Turbine to collect the results emitted by the flow
        searchViewModel.results.test {
            // Wait for the first PagingData emission
            val pagingData = awaitItem()

            // Use the helper to collect data from PagingData
            val differ = AsyncPagingDataDiffer(
                diffCallback = SearchResultDiffCallback(),
                updateCallback = NoopListUpdateCallback(), // Dummy callback to handle list updates
                mainDispatcher = testDispatcher,
                workerDispatcher = testDispatcher
            )

            // Submit the paging data to the differ to convert it to a list
            differ.submitData(pagingData)

            // Collect data from PagingDataDiffer as a list
            val collectedData = differ.snapshot().items

            // Assert that the collected data matches the expected list
            assertEquals(movieTvList, collectedData)

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `initial state is Initial`() = runTest {
        // Assert that the initial state of the ViewModel is Loading
        assertEquals(SearchState.Initial, searchViewModel.state.value)
    }

}

class SearchResultDiffCallback : DiffUtil.ItemCallback<SearchResult>() {
    override fun areItemsTheSame(oldItem: SearchResult, newItem: SearchResult): Boolean {
        return oldItem == newItem // Adjust based on your unique identifier
    }

    override fun areContentsTheSame(oldItem: SearchResult, newItem: SearchResult): Boolean {
        return oldItem == newItem // Adjust based on the content equality
    }
}

class NoopListUpdateCallback : ListUpdateCallback {
    override fun onInserted(position: Int, count: Int) { /* no-op */ }
    override fun onRemoved(position: Int, count: Int) { /* no-op */ }
    override fun onMoved(fromPosition: Int, toPosition: Int) { /* no-op */ }
    override fun onChanged(position: Int, count: Int, payload: Any?) { /* no-op */ }
}