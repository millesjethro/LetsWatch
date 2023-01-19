package com.auf.letswatch.services.repository

import com.auf.letswatch.models.informations.TitleDetails
import com.auf.letswatch.models.list.WatchmodeListModel
import com.auf.letswatch.models.search.SearchModel
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface watchmodeAPI {

    @GET("list-titles")
    suspend fun getWatchList(
        @Query("apiKey") apiKey:String,
        @Query("source_ids") id: Int,
        @Query("sort_by") sort: String,
        @Query("types") type: String,
        @Query("page") page:Int,
        @Query("limit") lim:Int
    ): Response<WatchmodeListModel>

    @GET("title/{title_id}/details")
    suspend fun getWatchtitle(
        @Path("title_id") titleid: String,
        @Query("apiKey") apiKey: String,
        @Query("append_to_response") append: String
    ): Response<TitleDetails>

    @GET("autocomplete-search")
    suspend fun getSearchList(
        @Query("apiKey") apiKey:String,
        @Query("search_value") searchfield: String,
        @Query("search_type") seachvalues: Int
    ): Response<SearchModel>


}