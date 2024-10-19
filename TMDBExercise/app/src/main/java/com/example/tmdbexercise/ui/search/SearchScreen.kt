package com.example.tmdbexercise.ui.search

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.IconButton
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
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
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.example.tmdbexercise.R
import com.example.tmdbexercise.common.Constants
import com.example.tmdbexercise.common.LoadingItem
import com.example.tmdbexercise.data.datasource.api.paging.SearchResult
import com.example.tmdbexercise.data.model.Movie
import com.example.tmdbexercise.data.model.TV
import com.example.tmdbexercise.ui.search.SearchViewModel.SearchType

@Composable
fun SearchScreen(
    navToDetails: (Int) -> Unit,
    viewModel: SearchViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()
    val items = viewModel.results.collectAsLazyPagingItems()

    var selectedOption by remember { mutableStateOf(SearchType.Movies) }
    var inputText by remember { mutableStateOf("") }

    SearchContent(
        state = state,
        navToDetails = navToDetails,
        items = items,
        selectedOption = selectedOption,
        onOptionSelected = { option ->
            selectedOption =
                when (option) {
                    "Search Movie" -> SearchType.Movies
                    "Search TV" -> SearchType.TV
                    else -> SearchType.Movies
                }
            viewModel.setSearchType(selectedOption)
            viewModel.searchMovies(inputText)
        },
        inputText = inputText,
        onTextChange = { text ->
            inputText = text // Update input text
            viewModel.setSearchType(selectedOption)
            viewModel.searchMovies(text) // Call searchMovies whenever text changes
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchContent(
    state: SearchState,
    items: LazyPagingItems<SearchResult>,
    navToDetails: (Int) -> Unit,
    selectedOption: SearchType,
    onOptionSelected: (String) -> Unit,
    inputText: String,
    onTextChange: (String) -> Unit
) {

    var showErrorDialog by remember { mutableStateOf(false) }
    var expanded by remember { mutableStateOf(false) }


    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        // Top row with screen title
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
                    text = stringResource(R.string.search_screen_title),
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
                    onClick = {},
                    enabled = false
                ) {}
            }
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // Dropdown for selecting options
            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = {
                    expanded = !expanded
                }
            ) {
                TextField(
                    value = when (selectedOption) {
                        SearchType.Movies -> stringResource(R.string.dropdown_item_search_movie)
                        SearchType.TV -> stringResource(R.string.dropdown_item_search_tv)
                        else -> stringResource(R.string.dropdown_item_search_movie)
                    },
                    onValueChange = {},
                    readOnly = true,
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor(MenuAnchorType.PrimaryNotEditable, true)
                )
                ExposedDropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    listOf(
                        stringResource(R.string.dropdown_item_search_movie),
                        stringResource(R.string.dropdown_item_search_tv)
                    )
                        .forEach { option ->
                            DropdownMenuItem(
                                text = { Text(option) },
                                onClick = {
                                    onOptionSelected(option)  // Pass selected option to parent
                                    expanded = false
                                }
                            )
                        }
                }
            }

            Spacer(modifier = Modifier.width(8.dp))

            // TextField for search input
            TextField(
                value = inputText,
                onValueChange = onTextChange,  // Update input text
                label = { Text(stringResource(R.string.text_input_search_hint)) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 20.dp)
            )
        }

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(16.dp)
        ) {
            items(
                count = items.itemCount,
                key = { it }
            ) { index ->
                val item = items[index]
                if (item != null) {
                    if (item is SearchResult.MovieResult) {
                        MovieCard(item.movie, navToDetails)
                    } else if (item is SearchResult.TVResult) {
                        TVCard(item.tv)
                    }
                } else {
                    Text(stringResource(R.string.search_list_empty_text))
                }
            }
            items.apply {
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

        if (state is SearchState.Initial) {
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.CenterHorizontally)
                    .padding(top = 16.dp),
                text = stringResource(R.string.initial_state_text)
            )
        }

        if (state is SearchState.Error) {
            showErrorDialog = true
        }
        if (state is SearchState.Loading) {
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
                    Text(text = stringResource(R.string.error_alert_dialog_title))
                },
                text = {
                    Text(
                        text = (state as? SearchState.Error)?.message
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

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun MovieCard(movie: Movie, navToDetails: (Int) -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        onClick = { navToDetails(movie.id) }
    ) {
        Row {
            GlideImage(
                modifier = Modifier
                    .weight(1.0f)
                    .fillMaxHeight()
                    .align(Alignment.CenterVertically)
                    .padding(start = 16.dp)
                    .clip(RoundedCornerShape(10.dp)),
                model = "${Constants.POSTER_URL}${movie.posterPath}",
                contentDescription = movie.title,
                alignment = Alignment.Center
            )
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .weight(2.0f)
            ) {
                Text(
                    movie.title ?: stringResource(R.string.no_title_available),
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    movie.overview ?: stringResource(R.string.no_description_available),
                    Modifier.padding(top = 16.dp)
                )
            }
        }
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun TVCard(tv: TV) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        onClick = {}
    ) {
        Row {
            GlideImage(
                modifier = Modifier
                    .weight(1.0f)
                    .fillMaxHeight()
                    .align(Alignment.CenterVertically)
                    .padding(start = 16.dp)
                    .clip(RoundedCornerShape(10.dp)),
                model = "${Constants.POSTER_URL}${tv.posterPath}",
                contentDescription = tv.name,
                alignment = Alignment.Center
            )

            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .weight(2.0f)
            ) {
                Text(
                    tv.name ?: stringResource(R.string.no_title_available),
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    tv.overview ?: stringResource(R.string.no_description_available),
                    Modifier.padding(top = 16.dp)
                )
            }
        }
    }
}