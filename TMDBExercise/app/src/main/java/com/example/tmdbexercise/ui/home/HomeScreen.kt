package com.example.tmdbexercise.ui.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import coil3.compose.AsyncImage
import com.example.tmdbexercise.data.model.Movie
import com.example.tmdbexercise.data.model.MovieList

@Composable
fun HomeScreen(
    navToDetails: (Int) -> Unit,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()
    val movies = viewModel.movies.collectAsLazyPagingItems()
    HomeContent(
        state = state,
        navToDetails = navToDetails,
        movies = movies,
    )
}

@Composable
fun HomeContent(
    state: HomeState,
    movies: LazyPagingItems<Movie>,
    navToDetails: (Int) -> Unit
) {
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
                onClick = {}
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
                    key = movies.itemKey{it.id}
                ) { index ->
                    val movie = movies[index]
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        onClick = {navToDetails(movie?.id ?: 0)}
                    ) {
                        Row {
                            AsyncImage(
                                modifier = Modifier.height(100.dp).width(100.dp),
                                model = "https://image.tmdb.org/t/p/w500${movie?.posterPath}",
                                contentDescription = movie?.title
                            )
                            Column {
                                Text(movie?.title ?: "")
                                Text(movie?.overview ?: "")
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

        }
        if (state is HomeState.Loading) {
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = androidx.compose.ui.Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }
    }
}

@Composable
fun LoadingItem() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(modifier = Modifier.size(32.dp), color = Color.Blue)
    }
}

//@Preview(showBackground = true)
//@Composable
//fun HomeScreenPreview() {
//    HomeContent(
//        state = HomeState.Success(
//            data = MovieList(
//                movies = listOf(
//                    Movie(
//                        id = 1,
//                        overview = "Overview",
//                        posterPath = "",
//                        title = "",
//                        releaseDate = ""
//                    ),
//                    Movie(
//                        id = 1,
//                        overview = "Overview",
//                        posterPath = "",
//                        title = "",
//                        releaseDate = ""
//                    )
//                )
//            )
//        ),
//        navToDetails = {}
//    )
//}

