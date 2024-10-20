package com.example.tmdbexercise.ui.details

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LifecycleRegistry
import androidx.lifecycle.SavedStateHandle
import androidx.navigation.toRoute
import app.cash.turbine.test
import com.example.tmdbexercise.common.DetailsScreen
import com.example.tmdbexercise.data.model.Movie
import com.example.tmdbexercise.domain.usecase.GetMovieUseCase
import com.example.tmdbexercise.domain.usecase.GetMoviesFromDBUseCase
import com.example.tmdbexercise.domain.usecase.RemoveMovieFromDBUseCase
import com.example.tmdbexercise.domain.usecase.SaveMovieToDBUseCase
import com.google.common.truth.Truth.assertThat
import io.mockk.*
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.test.*
import org.junit.*

class TestLifecycleOwner : LifecycleOwner {
    private val lifecycleRegistry = LifecycleRegistry(this)

    fun handleEvent(event: Lifecycle.Event) {
        lifecycleRegistry.handleLifecycleEvent(event)
    }

    override val lifecycle: Lifecycle
        get() = lifecycleRegistry
}

@ExperimentalCoroutinesApi
class DetailsViewModelTest {

    // Rule to allow LiveData to work properly in tests
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    // Test dispatcher to control coroutine execution
    private val testDispatcher = UnconfinedTestDispatcher()

    // Mock use cases
    private val movieUseCase: GetMovieUseCase = mockk()
    private val saveMovieToDBUseCase: SaveMovieToDBUseCase = mockk(relaxed = true)
    private val getMoviesFromDBUseCase: GetMoviesFromDBUseCase = mockk(relaxed = true)
    private val removeMovieFromDBUseCase: RemoveMovieFromDBUseCase = mockk(relaxed = true)

    // SavedStateHandle mock
    private val savedStateHandle = mockk<SavedStateHandle>(relaxed = true)

    // ViewModel under test
    private lateinit var detailsViewModel: DetailsViewModel
    private lateinit var testLifecycleOwner: TestLifecycleOwner

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        testLifecycleOwner = TestLifecycleOwner()

        mockkStatic("androidx.navigation.SavedStateHandleKt")
        every { savedStateHandle.toRoute<DetailsScreen>() } returns DetailsScreen(movieId = 1)

        detailsViewModel = DetailsViewModel(
            movieUseCase,
            saveMovieToDBUseCase,
            getMoviesFromDBUseCase,
            removeMovieFromDBUseCase,
            savedStateHandle
        )
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        clearAllMocks()
    }

    @Test
    fun `getMovie should emit Success state when movie is fetched successfully`() = runTest {
        val movie = Movie(1, "Overview", "PosterPath", "Title", "ReleaseDate", 8.5, 100)

        coEvery { movieUseCase.execute(any()) } returns Result.success(movie)

        detailsViewModel.getMovie()
        detailsViewModel.state.test {
            assertEquals(DetailsState.Success(movie), awaitItem())
        }

    }

    @Test
    fun `getMovie should emit Error state when movie fetch fails`() = runTest {

        coEvery { movieUseCase.execute(any()) } returns Result.failure(Exception("Fetch failed"))

        detailsViewModel.getMovie()
        detailsViewModel.state.test {
            assertEquals(DetailsState.Error("Fetch failed"), awaitItem())
        }
    }

    @Test
    fun `saveMovie should save movie to DB and set isFavorite to true`() = runTest {
        val movie = Movie(1, "Overview", "PosterPath", "Title", "ReleaseDate", 8.5, 100)

        detailsViewModel.saveMovie(movie)

        detailsViewModel.isFavorite.test {
            assertEquals(true, awaitItem())
        }
    }

    @Test
    fun `removeMovieFromFavourites should remove movie from DB and set isFavorite to false`() = runTest {
        val movie = Movie(1, "Overview", "PosterPath", "Title", "ReleaseDate", 8.5, 100)

        detailsViewModel.saveMovie(movie)

        detailsViewModel.removeMovieFromFavourites(1)

        detailsViewModel.isFavorite.test {
            assertEquals(false, awaitItem())
        }
    }

    @Test
    fun `isMovieFavorite should return true when movie is in DB`() = runTest {
        val movie = Movie(1, "Overview", "PosterPath", "Title", "ReleaseDate", 8.5, 100)

        detailsViewModel.saveMovie(movie)

        detailsViewModel.isMovieFavorite(1)

        detailsViewModel.isFavorite.test {
            assertEquals(true, awaitItem())
        }
    }

    @Test
    fun `isMovieFavorite should return false when movie is not in DB`() = runTest {
        val movie = Movie(1, "Overview", "PosterPath", "Title", "ReleaseDate", 8.5, 100)
        detailsViewModel.saveMovie(movie)
        detailsViewModel.removeMovieFromFavourites(1)
        detailsViewModel.isMovieFavorite(1)

        detailsViewModel.isFavorite.test {
            assertEquals(false, awaitItem())
        }
    }
}