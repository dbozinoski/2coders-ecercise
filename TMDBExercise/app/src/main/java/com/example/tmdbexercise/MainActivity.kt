package com.example.tmdbexercise

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.tmdbexercise.common.ComposeExampleScreen
import com.example.tmdbexercise.common.DetailsScreen
import com.example.tmdbexercise.common.HomeScreen
import com.example.tmdbexercise.common.SearchScreen
import com.example.tmdbexercise.ui.details.DetailsScreen
import com.example.tmdbexercise.ui.home.HomeScreen
import com.example.tmdbexercise.ui.search.SearchScreen
import com.example.tmdbexercise.ui.theme.TMDBExerciseTheme
import com.example.tmdbexercise.ui.excompose.ComposeExampleScreen;
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val navController = rememberNavController()
            TMDBExerciseTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    innerPadding
                    NavHost(navController = navController, startDestination = HomeScreen("")) {
                        composable<HomeScreen> {
                            HomeScreen(
                                navToDetails = { movieId ->
                                    navController.navigate(DetailsScreen(movieId))
                                },
                                navToSearch = {
                                    navController.navigate(SearchScreen(""))
                                },
                                navToComposeExamples = {
                                    navController.navigate(ComposeExampleScreen(""))
                                }
                            )
                        }
                        composable<DetailsScreen> {
                            DetailsScreen()
                        }
                        composable<SearchScreen> {
                            SearchScreen(navToDetails = { movieId ->
                                navController.navigate(DetailsScreen(movieId))
                            })
                        }
                        composable<ComposeExampleScreen> {
                            ComposeExampleScreen(
                                navToDetails = { movieId ->
                                    navController.navigate(DetailsScreen(movieId))
                                },
                                navToSearch = {
                                    navController.navigate(SearchScreen(""))
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}