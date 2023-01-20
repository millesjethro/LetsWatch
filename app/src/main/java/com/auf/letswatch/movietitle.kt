package com.auf.letswatch

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.auf.letswatch.adapters.MovieInformationAdapters.ShowTitlesAdapter
import com.auf.letswatch.constants.API_KEY
import com.auf.letswatch.databinding.FragmentMovietitleBinding
import com.auf.letswatch.models.informations.Source
import com.auf.letswatch.models.informations.TitleDetails
import com.auf.letswatch.services.helper.RetrofitHelper
import com.auf.letswatch.services.realm.config.RealmConfig
import com.auf.letswatch.services.realm.operation.Operations
import com.auf.letswatch.services.repository.watchmodeAPI
import com.bumptech.glide.Glide
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import io.realm.Realm
import io.realm.RealmConfiguration
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext


class movietitle : DialogFragment(){
    private var _binding: FragmentMovietitleBinding? = null
    private val binding get() = _binding!!
    private lateinit var movieData: ArrayList<TitleDetails>
    private lateinit var movieSources: ArrayList<Source>
    private lateinit var sources: ShowTitlesAdapter
    private lateinit var config: RealmConfiguration
    private lateinit var operation: Operations
    private lateinit var coroutine: CoroutineContext
    private lateinit var database: DatabaseReference
    private lateinit var favfrag: Favorite
    private var usernm: String = ""


    @DelicateCoroutinesApi
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentMovietitleBinding.inflate(inflater, container, false)
        val movieID = requireArguments().getString("titleID")
        movieData = arrayListOf()
        movieSources = arrayListOf()

        sources = ShowTitlesAdapter(movieSources,requireContext())
        val layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL,false)
        binding.sourcesrv.layoutManager = layoutManager
        binding.sourcesrv.adapter = sources

        favfrag = Favorite()
        if (movieID != null) {
            Log.e("",movieID)
            getMovie(movieID)
        }

        dialog?.window?.requestFeature(Window.FEATURE_NO_TITLE);
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.BLACK));

        val sharedPreferences = this.activity?.getSharedPreferences("MY_PROFILE", Context.MODE_PRIVATE)
        val username = sharedPreferences?.getString(USERNAME_KEY,"")

        usernm = username.toString()

        Realm.init(requireContext())

        config = RealmConfig.getConfiguration()
        operation = Operations(config)
        coroutine = Job() + Dispatchers.IO


        database = FirebaseDatabase.getInstance().getReferenceFromUrl("https://letswatch-de7ce-default-rtdb.firebaseio.com/")

        return binding.root
    }

    @DelicateCoroutinesApi
    private fun getMovie(titleID: String){
        val movieApi = RetrofitHelper.getInstance().create(watchmodeAPI::class.java)
        GlobalScope.launch(Dispatchers.IO) {
            val result = movieApi.getWatchtitle(titleID, API_KEY,"sources")
            val movieDataResult = result.body()
            if (movieDataResult != null) {
                movieData.addAll(listOf(movieDataResult))
                movieSources.addAll(movieDataResult.sources)
                withContext(Dispatchers.Main) {
                    if(movieSources.size <= 0){
                        binding.sourcess.text = "NO SOURCES"
                    }
                    else{
                        sources.updateData(movieSources)
                    }
                    binding.txtRuntime.text = movieData[0].runtime_minutes.toString()+" mins"
                    binding.txtTitle.text = movieData[0].title
                    Glide.with(binding.root)
                        .load(movieData[0].poster)
                        .into(binding.imgTitle)
                    binding.plotOverview.text = movieData[0].plot_overview.toString()

                    if(movieData[0].end_year>0){
                        binding.txtStatus.text = "Completed"
                        binding.txtEndyear.text = movieData[0].end_year.toString()
                    }
                    else{
                        binding.txtStatus.text = "Ongoing"
                        binding.txtEndyear.text = "No End Year Yet"

                    }
                    if(movieData[0].genre_names.isNotEmpty()){
                        binding.txtGenre.text = movieData[0].genre_names[0]
                    }

                    else{
                        binding.txtGenre.text = ""
                    }

                    if(movieData[0].network_names.isNotEmpty()){
                        binding.txtNetwork.text = movieData[0].network_names[0]
                    }
                    else{
                        binding.txtNetwork.text = ""
                    }
                    binding.btnTrailer.setOnClickListener{
                        val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(movieData[0].trailer))
                        startActivity(browserIntent)
                    }

                    val realmConfig = RealmConfig.getConfiguration()
                    val operations = Operations(realmConfig)
                    val coroutine = Job() + Dispatchers.IO
                    val scope = CoroutineScope(coroutine)

                    scope.launch(Dispatchers.Main){
                        val filterResult = operations.filterBrew(movieData[0].id,usernm)
                        Log.e("",filterResult.size.toString())
                        if (filterResult.size != 0) {
                            binding.btnFav.tag = "favorited"
                            binding.btnFav.setImageResource(R.drawable.ic_added_to_favorite)
                        }
                        else{
                            binding.btnFav.tag = "favorite"
                            binding.btnFav.setImageResource(R.drawable.ic_add_to_favorite)
                        }

                    }

                    binding.txtType.text = movieData[0].type
                    binding.txtRelease.text = movieData[0].release_date
                    binding.txtCriticScore.text = movieData[0].critic_score.toString()
                    binding.txtUserRating.text = movieData[0].user_rating.toString()


                    binding.btnFav.setOnClickListener {
                        if(binding.btnFav.tag.equals("favorite")) {
                            binding.btnFav.setImageResource(R.drawable.ic_added_to_favorite)
                            binding.btnFav.tag = "favorited"
                            addBrew(
                                usernm,
                                movieData[0].title,
                                movieData[0].poster,
                                binding.txtStatus.text.toString(),
                                movieData[0].release_date,
                                movieData[0].end_year,
                                movieData[0].user_rating,
                                movieData[0].id
                            )
                        }
                        else{
                            binding.btnFav.setImageResource(R.drawable.ic_add_to_favorite)
                            binding.btnFav.tag = "favorited"
                            removeBrew(movieData[0].id)
                        }
                    }
                }
            }
        }
    }

    fun addBrew(username: String, title: String, poster: String, status: String, release_date: String, end_year: Int, user_rating: Double, id: String) {
        val scope = CoroutineScope(coroutine)
        scope.launch(Dispatchers.IO){
            operation.InsertMovie(username, status, end_year, id, release_date, title,poster,user_rating)
            withContext(Dispatchers.Main){
            }
        }
    }
    fun removeBrew(id: String) {
        val scope = CoroutineScope(coroutine)
        scope.launch(Dispatchers.IO){
            operation.removeMovie(id)
        }
    }

}