package com.auf.letswatch.models.informations


data class TitleDetails(
    val backdrop: String,
    val critic_score: Int,
    val end_year: Int,
    val genre_names: List<String>,
    val genres: List<Int>,
    val id: String,
    val imdb_id: String,
    val network_names: List<String>,
    val networks: List<Int>,
    val original_language: String,
    val original_title: String,
    val plot_overview: String,
    val poster: String,
    val release_date: String,
    val relevance_percentile: Double,
    val runtime_minutes: Int,
    val similar_titles: List<Int>,
    val sources: List<Source>,
    val title: String,
    val tmdb_id: Int,
    val tmdb_type: String,
    val trailer: String,
    val trailer_thumbnail: String,
    val type: String,
    val us_rating: String,
    val user_rating: Double,
    val year: Int
)