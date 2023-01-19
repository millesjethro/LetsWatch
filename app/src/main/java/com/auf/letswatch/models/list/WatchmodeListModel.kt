package com.auf.letswatch.models.list

data class WatchmodeListModel(
    val page: Int,
    val titles: List<Title>,
    val total_pages: Int,
    val total_results: Int
): java.io.Serializable