package com.auf.letswatch.models.search

data class Result(
    val id: Int,
    val image_url: String,
    val name: String,
    val relevance: Double,
    val result_type: String,
    val tmdb_id: Int,
    val tmdb_type: String,
    val type: String,
    val year: Int
)