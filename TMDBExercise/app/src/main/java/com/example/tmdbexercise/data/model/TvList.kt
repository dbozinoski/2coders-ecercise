package com.example.tmdbexercise.data.model

import com.google.gson.annotations.SerializedName

data class TvList(
    @SerializedName("results")
    val tvList: List<TV>,
    @SerializedName("page")
    val page: Int,
    @SerializedName("total_pages")
    val totalPages: Int,
    @SerializedName("total_results")
    val totalResults: Int,
)
