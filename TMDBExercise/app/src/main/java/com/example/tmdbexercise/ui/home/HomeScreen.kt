package com.example.tmdbexercise.ui.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.example.tmdbexercise.common.LoadingItem
import com.example.tmdbexercise.data.model.Movie
import com.example.tmdbexercise.data.model.MovieList
import com.example.tmdbexercise.ui.details.DetailsState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

@Composable
fun HomeScreen(
    navToDetails: (Int) -> Unit,
    navToSearch: (Int) -> Unit,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()
    val movies = viewModel.movies.collectAsLazyPagingItems()
    HomeContent(
        state = state,
        navToDetails = navToDetails,
        movies = movies,
        navToSearch = navToSearch
    )
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun HomeContent(
    state: HomeState,
    movies: LazyPagingItems<Movie>,
    navToDetails: (Int) -> Unit,
    navToSearch: (Int) -> Unit
) {
    var showErrorDialog by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.End
        ) {
            IconButton(
                onClick = {navToSearch(0)}
            ) {
                Icon(Icons.Default.Search, contentDescription = "Search")
            }
        }

//        if (state is HomeState.Success) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(16.dp)
        ) {
            items(
                count = movies.itemCount,
                key = movies.itemKey { it.id }
            ) { index ->
                val movie = movies[index]
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = { navToDetails(movie?.id ?: 0) }
                ) {
                    Row {
                        GlideImage(
                            modifier = Modifier
                                .weight(1.0f)
                                .fillMaxHeight()
                                .align(Alignment.CenterVertically)
                                .padding(start = 16.dp),
                            model = "https://image.tmdb.org/t/p/w500${movie?.posterPath}",
                            contentDescription = movie?.title,
                            alignment = Alignment.Center
                        )
                        Column(
                            modifier = Modifier
                                .padding(16.dp)
                                .weight(2.0f)
                        ) {
                            Text(
                                movie?.title ?: "",
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                movie?.overview ?: "",
                                Modifier.padding(top = 16.dp)
                            )
                        }
                    }

                }
            }
            movies.apply {
                when {
                    loadState.refresh is LoadState.Loading -> {
                        item { LoadingItem() }
                    }

                    loadState.append is LoadState.Loading -> {
                        item { LoadingItem() }
                    }
                }
            }

        }
//        }
        if (state is HomeState.Error) {
            showErrorDialog = true
        }
        if (state is HomeState.Loading) {
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                LoadingItem()
            }
        }

        // Show error dialog if state is Error
        if (showErrorDialog) {
            AlertDialog(
                onDismissRequest = {
                    showErrorDialog = false // Close the dialog on dismiss
                },
                title = {
                    Text(text = "Error")
                },
                text = {
                    Text(
                        text = (state as? DetailsState.Error)?.message
                            ?: "An unknown error occurred"
                    )
                },
                confirmButton = {
                    Button(
                        onClick = {
                            showErrorDialog = false // Close the dialog on confirm
                        }
                    ) {
                        Text("OK")
                    }
                }
            )
        }
    }
}


// A function that simulates static PagingData
fun fakeMovieFlow(): Flow<PagingData<Movie>> {
    val movies = listOf(
        Movie(
            1,
            "Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book. It has survived not only five centuries, but also the leap into electronic typesetting, remaining essentially unchanged. It was popularised in the 1960s with the release of Letraset sheets containing Lorem Ipsum passages, and more recently with desktop publishing software like Aldus PageMaker including versions of Lorem Ipsum.",
            "https://image.tmdb.org/t/p/w500/lqoMzCcZYEFK729d6qzt349fB4o.jpg",
            "",
            "Inception"
        ),
        Movie(2, "", "", "", "The Dark Knight"),
        Movie(3, "", "", "", "Interstellar")
    )
    return flowOf(PagingData.from(movies))
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {

    HomeContent(
        state = HomeState.Success(
            data = MovieList(
                movies = listOf(
                    Movie(
                        id = 1,
                        overview = "Overview",
                        posterPath = "",
                        title = "",
                        releaseDate = ""
                    ),
                    Movie(
                        id = 1,
                        overview = "Overview",
                        posterPath = "",
                        title = "",
                        releaseDate = ""
                    )
                ),
                page = 1,
                totalPages = 1,
                totalResults = 10
            )
        ),
        navToDetails = {},
        movies = fakeMovieFlow().collectAsLazyPagingItems(),
        navToSearch = {}
    )
}
