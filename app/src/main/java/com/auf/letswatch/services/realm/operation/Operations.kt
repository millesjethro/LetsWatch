package com.auf.letswatch.services.realm.operation

import com.auf.letswatch.models.MovieDB.MovieDataBase
import com.auf.letswatch.services.realm.database.MovieRealm
import io.realm.Case
import io.realm.Realm
import io.realm.RealmConfiguration
import io.realm.kotlin.executeTransactionAwait
import kotlinx.coroutines.Dispatchers


class Operations(private var config: RealmConfiguration) {
    suspend fun InsertMovie(username: String, stats:String, end_year: Int, id:String, release:String,title:String,poster:String,user_rating:Double){
        val realm = Realm.getInstance(config)
        realm.executeTransactionAwait(Dispatchers.IO) { realmTransaction ->
            val Movie = MovieRealm(iduser = id+username,username = username, status = stats, end_year = end_year,id = id, release_date = release, poster = poster, title = title, user_rating = user_rating)
            realmTransaction.insert(Movie)
        }
    }

    suspend fun filterBrew(id: String,username: String): ArrayList<MovieDataBase>{
        val realm = Realm.getInstance(config)
        val realmResults = arrayListOf<MovieDataBase>()
        realm.executeTransactionAwait(Dispatchers.IO){ realmTransaction ->
            realmResults.addAll(realmTransaction
                .where(MovieRealm::class.java)
                .contains("iduser",id+username, Case.SENSITIVE)
                .findAll()
                .map{
                    mapMovie(it)
                })

        }
        return realmResults
    }

    suspend fun retrieveMovie(): ArrayList<MovieDataBase>{
        val realm = Realm.getInstance(config)
        val realmResults = arrayListOf<MovieDataBase>()

        realm.executeTransactionAwait(Dispatchers.IO) { realmTransaction ->
            realmResults.addAll(realmTransaction
                .where(MovieRealm::class.java)
                .findAll()
                .map{
                    mapMovie(it)
                })
        }
        return realmResults
    }

    suspend fun retrieveSpecificUser(username: String): List<MovieDataBase> {
        val realm = Realm.getInstance(config)
        val realmResults = mutableListOf<MovieDataBase>()

        realm.executeTransactionAwait(Dispatchers.IO) { realmTransaction ->
            realmResults.addAll(realmTransaction
                .where(MovieRealm::class.java)
                .equalTo("username", username)
                .findAll()
                .map {
                    mapMovie(it)
                }
            )
        }
        return realmResults
    }

    suspend fun removeMovie(id : String){
        val realm = Realm.getInstance(config)
        realm.executeTransactionAwait(Dispatchers.IO){ realmTransaction ->
            val movieToRemove = realmTransaction
                .where(MovieRealm::class.java)
                .equalTo("id",id)
                .findFirst()
            movieToRemove?.deleteFromRealm()
        }
    }

    private fun mapMovie(Movie: MovieRealm): MovieDataBase{
        return MovieDataBase(
            id = Movie.id,
            username = Movie.username,
            end_year = Movie.end_year,
            release_date = Movie.release_date,
            title = Movie.title,
            user_rating = Movie.user_rating,
            status = Movie.status,
            poster = Movie.poster
        )
    }
}