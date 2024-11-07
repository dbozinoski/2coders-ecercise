package com.example.tmdbexercise.ui.excompose

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.example.tmdbexercise.R
import com.example.tmdbexercise.common.Constants
import com.example.tmdbexercise.common.LoadingItem
import com.example.tmdbexercise.data.model.Movie
import com.example.tmdbexercise.ui.home.HomeState
import kotlin.random.Random

@Composable
fun ComposeExampleScreen(
    navToDetails: (Int) -> Unit,
    navToSearch: (Int) -> Unit,
    viewModel: ComposeExampleViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()
    val movies = viewModel.movies.collectAsLazyPagingItems()
    ComposeExampleContent(
        state = state,
        navToDetails = navToDetails,
        movies = movies,
        navToSearch = navToSearch
    )
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun ComposeExampleContent(
    state: ComposeExampleState,
    movies: LazyPagingItems<Movie>,
    navToDetails: (Int) -> Unit,
    navToSearch: (Int) -> Unit
) {
    val pageCount = movies.itemCount // total number of pages
    var showErrorDialog by remember { mutableStateOf(false) }
    val pagerState = rememberPagerState { pageCount }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
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
                    text = "Composables",
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
                    onClick = { navToSearch(0) }
                ) {
                    Icon(Icons.Default.Search, contentDescription = "Search")
                }
            }
        }

        HorizontalPager(
            state = pagerState,
            modifier = Modifier.fillMaxWidth(),
            pageSpacing = 10.dp
        ) { page ->
            val movie = remember { movies[page] }
            Card(
                modifier = Modifier.fillMaxWidth().fillMaxHeight(fraction = 0.4f),
                onClick = { navToDetails(movie?.id ?: 0) }
            ) {
                Row {
                    GlideImage(
                        modifier = Modifier
                            .weight(0.3f)
                            .fillMaxHeight()
                            .align(Alignment.CenterVertically)
                            .padding(start = 16.dp)
                            .clip(RoundedCornerShape(10.dp)),
                        model = "${Constants.POSTER_URL}${movie?.posterPath}",
                        contentDescription = movie?.title,
                        alignment = Alignment.Center,

                        )
                    Column(
                        modifier = Modifier
                            .padding(16.dp)
                            .weight(0.7f)
                    ) {
                        Text(
                            movie?.title ?: stringResource(R.string.no_title_available),
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            movie?.overview
                                ?: stringResource(R.string.no_description_available),
                            Modifier.padding(top = 16.dp)
                        )
                    }
                }
                movies.apply {
                    when {
                        loadState.refresh is LoadState.Loading -> {
                            LoadingItem()
                        }

                        loadState.append is LoadState.Loading -> {
                            LoadingItem()
                        }
                    }
                }

            }
        }

//        if (state is ComposeExampleState.Success)
//        }
        if (state is ComposeExampleState.Error) {
            showErrorDialog = true
        }
        if (state is ComposeExampleState.Loading) {
//            Box(
//                modifier = Modifier.fillMaxWidth(),
//                contentAlignment = Alignment.Center
//            ) {
//                LoadingItem()
//            }
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
                        text = (state as? HomeState.Error)?.message
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