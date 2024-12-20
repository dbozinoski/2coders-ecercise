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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.example.tmdbexercise.R
import com.example.tmdbexercise.common.Constants

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
    val isFavorite by viewModel.isFavorite.collectAsStateWithLifecycle()

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
        // Top row with favorite button
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 26.dp, start = 16.dp, end = 16.dp),
            horizontalArrangement = Arrangement.End
        ) {
            Box(
                modifier = Modifier
                    .weight(1.0f)
                    .align(Alignment.CenterVertically)
            )
            Box(
                modifier = Modifier
                    .weight(3.0f)
                    .align(Alignment.CenterVertically)
            ) {
                Text(
                    text = stringResource(R.string.details_screen_title),
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.align(Alignment.Center)
                )
            }
            Box(
                modifier = Modifier
                    .weight(1.0f)
                    .align(Alignment.CenterVertically),
            ) {
                IconButton(
                    modifier = Modifier.align(Alignment.CenterEnd),
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
        }

        // If movie is available, display its details
        movie?.let { movie ->
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState()),

            ) {
                Text(
                    modifier = Modifier
                        .padding(bottom = 20.dp)
                        .align(Alignment.CenterHorizontally),
                    text = movie.title ?: stringResource(R.string.no_title_available),
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                )
                GlideImage(
                    modifier = Modifier
                        .height(300.dp)
                        .width(300.dp)
                        .align(Alignment.CenterHorizontally)
                        .clip(RoundedCornerShape(10.dp)),
                    model = "${Constants.POSTER_URL}${movie.posterPath}",
                    contentDescription = movie.title
                )
                Column(
                    modifier = Modifier
                        .padding(top = 20.dp)
                        .align(Alignment.Start)
                )
                {
                    Text(
                        modifier = Modifier.padding(end = 16.dp),
                        text = stringResource(R.string.details_overview_text),
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        modifier = Modifier.padding(top = 16.dp),
                        text = movie.overview ?: stringResource(R.string.no_description_available),
                        textAlign = TextAlign.Justify
                    )

                }
                Row(
                    modifier = Modifier
                        .padding(top = 20.dp)
                        .align(Alignment.Start)
                ){
                    Text(
                        modifier = Modifier.padding(end = 5.dp),
                        text = stringResource(R.string.details_release_date_text),
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = movie.releaseDate ?: stringResource(R.string.no_release_date_available)
                    )
                }
                Row(
                    modifier = Modifier
                        .padding(top = 20.dp)
                        .align(Alignment.Start)
                ){
                    Text(
                        modifier = Modifier.padding(end = 5.dp),
                        text = stringResource(R.string.details_rating_text),
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = movie.rating.toString()
                    )
                }
                Row(
                    modifier = Modifier
                        .padding(top = 20.dp)
                        .align(Alignment.Start)
                ){
                    Text(
                        modifier = Modifier.padding(end = 5.dp),
                        text = stringResource(R.string.details_votes_text),
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = movie.votes.toString()
                    )
                }

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
                    Text(text = stringResource(R.string.error_alert_dialog_title))
                },
                text = {
                    Text(
                        text = (state as? DetailsState.Error)?.message
                            ?: stringResource(R.string.error_alert_dialog_description)
                    )
                },
                confirmButton = {
                    Button(
                        onClick = {
                            showErrorDialog = false // Close the dialog on confirm
                        }
                    ) {
                        Text(stringResource(R.string.error_alert_dialog_ok_button_text))
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