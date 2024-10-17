package com.example.tmdbexercise.ui.details

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil3.compose.AsyncImage
import com.example.tmdbexercise.data.model.Movie

@Composable
fun DetailsScreen(
    viewModel: DetailsViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()
    DetailsContent(
        state = state
    )
}

@Composable
fun DetailsContent(
    state: DetailsState
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
            Column {
                IconButton(
                    onClick = {}
                ) {
                    Icon(
                        Icons.Default.Favorite,
                        contentDescription = "Favorite",
                    )
                }
            }
            Column {
                IconButton(
                    onClick = {}
                ) {
                    Icon(Icons.Default.Search, contentDescription = "Search")
                }
            }
        }

        if (state is DetailsState.Success) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(state.movie.title ?: "")
                AsyncImage(
                    modifier = Modifier.height(100.dp).width(100.dp),
                    model = "https://image.tmdb.org/t/p/w500${state.movie.posterPath}",
                    contentDescription = state.movie.title
                )
                Text(state.movie.overview ?: "")
            }
        }
        if (state is DetailsState.Error) {

        }
        if (state is DetailsState.Loading) {
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = androidx.compose.ui.Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DetailsScreenPreview() {
    DetailsContent(
        state = DetailsState.Success(
            movie = Movie(
                id = 1,
                overview = "Overview",
                posterPath = "",
                title = "",
                releaseDate = ""

            )
        )
    )
}