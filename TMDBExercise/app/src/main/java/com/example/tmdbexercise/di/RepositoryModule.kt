package com.example.tmdbexercise.di

import com.example.tmdbexercise.data.repository.datasource.FavouriteMovieLocalDataSource
import com.example.tmdbexercise.data.repository.datasourceImpl.FavouriteMovieLocalDataSourceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindFavouriteMovieLocalDataSource(
        impl: FavouriteMovieLocalDataSourceImpl
    ): FavouriteMovieLocalDataSource
}