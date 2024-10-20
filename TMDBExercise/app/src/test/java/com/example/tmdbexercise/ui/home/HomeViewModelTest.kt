package com.example.tmdbexercise.ui.home

import androidx.paging.PagingData
import androidx.paging.testing.asSnapshot
import com.example.tmdbexercise.data.model.Movie
import com.example.tmdbexercise.domain.usecase.GetMoviesUseCase
import io.mockk.*
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Before
import org.junit.Test


@ExperimentalCoroutinesApi
class HomeViewModelTest {

    // A test dispatcher to run coroutines in a controlled way
    private val testDispatcher = UnconfinedTestDispatcher()

    // Step 1: Create a sample list of movies
    val movieList = listOf(
        Movie(1, "Overview 1", "PosterPath 1", "Title 1", "ReleaseDate 1", 8.5, 100),
        Movie(2, "Overview 2", "PosterPath 2", "Title 2", "ReleaseDate 2", 7.9, 200)
    )

    // Step 2: Convert the list to PagingData using PagingData.from()
    val mockPagingData = PagingData.from(movieList)

    // Step 3: Mock the Flow<PagingData<Movie>> to return the mockPagingData
    val mockFlow: Flow<PagingData<Movie>> = flowOf(mockPagingData)

    private val mockUseCase = mockk<GetMoviesUseCase>(relaxed = true) {
        every { this@mockk.invoke() } returns mockFlow
    }

    // The ViewModel under test
    private lateinit var homeViewModel: HomeViewModel


    @Before
    fun setup() {
        // Set the test dispatcher as the main dispatcher for coroutine contexts
        Dispatchers.setMain(testDispatcher)

        // Initialize the ViewModel
        homeViewModel = HomeViewModel(mockUseCase)

    }

    @After
    fun tearDown() {
        // Reset the main dispatcher to its original state
        Dispatchers.resetMain()
    }

    @Test
    fun `movies flow emits paging data from use case`() = runTest(){

        val items = homeViewModel.movies
        val itemsSnapshot = items.asSnapshot()

        assertEquals(itemsSnapshot, movieList)

    }

    @Test
    fun `initial state is Loading`() = runTest {
        // Assert that the initial state of the ViewModel is Loading
        assertEquals(HomeState.Loading, homeViewModel.state.value)
    }
}