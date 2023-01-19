package com.auf.letswatch.services.realm.database

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import io.realm.annotations.Required
import org.bson.types.ObjectId

open class MovieRealm(
    @PrimaryKey
    var iduser: String= "",
    @Required
    var id:String = "",
    var username: String= "",
    var end_year: Int=0,
    var poster: String= "",
    var release_date: String= "",
    var title: String= "",
    var type: String= "",
    var user_rating: Double= 0.0,
    var status: String = ""
    ) : RealmObject()
