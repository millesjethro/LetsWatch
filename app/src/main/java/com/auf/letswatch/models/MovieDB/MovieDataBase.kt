package com.auf.letswatch.models.MovieDB

import io.realm.RealmModel
import java.io.Serializable

data class MovieDataBase(
    var id:String = "",
    val username: String,
    val title: String,
    val poster: String,
    val status: String,
    val release_date: String,
    val end_year: Int,
    val user_rating: Double
) : Serializable
