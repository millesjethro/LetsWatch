package com.auf.letswatch.models.informations

data class Source(
    val android_url: String,
    val episodes: Int,
    val format: String,
    val ios_url: String,
    val name: String,
    val price: Double,
    val region: String,
    val seasons: Int,
    val source_id: Int,
    val type: String,
    val web_url: String
)