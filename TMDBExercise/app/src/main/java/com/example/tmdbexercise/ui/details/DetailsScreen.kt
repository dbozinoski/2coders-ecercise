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
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage

@Composable
fun DetailsScreen(
    viewModel: DetailsViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()
    DetailsContent(
        state = state,
        viewModel = viewModel
    )
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun DetailsContent(
    state: DetailsState,
    viewModel: DetailsViewModel
) {

    var showErrorDialog by remember { mutableStateOf(false) }

    val movie = if (state is DetailsState.Success) {
        state.movie
    } else {
        null // The movie is null if state is not Success
    }

    // Observe the isFavorite LiveData from the ViewModel
    val isFavorite by viewModel.isFavorite.observeAsState(initial = false)

    // Check if the movie is already a favorite using LaunchedEffect
    LaunchedEffect(movie) {
        movie?.let {
            // Query the ViewModel to check if the movie is already a favorite
            viewModel.isMovieFavorite(movie.id)
        }
    }

    // UI layout
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        // Top row with favorite and search buttons
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.End
        ) {
            Column {
                IconButton(
                    onClick = {
                        if (movie != null) {
                            if (isFavorite) {
                                viewModel.removeMovieFromFavourites(movie.id)
                            } else {
                                viewModel.saveMovie(movie)
                            }
                        }
                    }
                ) {
                    Icon(
                        imageVector = if (isFavorite) {
                            Icons.Default.Favorite
                        } else {
                            Icons.Default.FavoriteBorder
                        },
                        contentDescription = "Favorite",
                    )
                }
            }
//            Column {
//                IconButton(
//                    onClick = {}
//                ) {
//                    Icon(Icons.Default.Search, contentDescription = "Search")
//                }
//            }
        }

        // If movie is available, display its details
        movie?.let { movie ->
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    modifier = Modifier
                        .padding(bottom = 20.dp)
                        .align(Alignment.CenterHorizontally),
                    text = movie.title ?: "",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                )
                GlideImage(
                    modifier = Modifier
                        .height(300.dp)
                        .width(300.dp)
                        .align(Alignment.CenterHorizontally),
                    model = "https://image.tmdb.org/t/p/w500${movie.posterPath}",
                    contentDescription = movie.title
                )
                Text(
                    modifier = Modifier
                    .padding(top = 20.dp)
                    .align(Alignment.CenterHorizontally),
                    text = movie.overview ?: "",
                    textAlign = TextAlign.Justify
                )
            }
        }

        // Loading and Error states
        when (state) {
            is DetailsState.Loading -> {
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }

            is DetailsState.Error -> {
                showErrorDialog = true
            }

            else -> Unit // Do nothing for other states
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
                    Text(text = (state as? DetailsState.Error)?.message ?: "An unknown error occurred")
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

//@Preview(showBackground = true)
//@Composable
//fun DetailsScreenPreview() {
//    DetailsContent(
//        state = DetailsState.Success(
//            movie = Movie(
//                id = 1,
//                overview = "Overview",
//                posterPath = "",
//                title = "",
//                releaseDate = ""
//
//            )
//        )
//    )
//}