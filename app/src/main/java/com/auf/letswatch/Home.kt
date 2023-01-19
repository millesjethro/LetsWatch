package com.auf.letswatch

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.auf.letswatch.adapters.HomeAdapters.MovieListAdapter
import com.auf.letswatch.adapters.HomeAdapters.NetflixAdapter
import com.auf.letswatch.adapters.HomeAdapters.SeriesAdapter
import com.auf.letswatch.adapters.HomeAdapters.VuduAdapter
import com.auf.letswatch.constants.API_KEY
import com.auf.letswatch.constants.NETFLIX
import com.auf.letswatch.constants.VUDU
import com.auf.letswatch.databinding.FragmentHomeBinding
import com.auf.letswatch.models.list.Title
import com.auf.letswatch.services.helper.RetrofitHelper
import com.auf.letswatch.services.repository.watchmodeAPI
import kotlinx.coroutines.*


class Home : Fragment() {
    private lateinit var MovieAdapter: MovieListAdapter
    private lateinit var NetflixAdapter: NetflixAdapter
    private lateinit var SeriesAdapter: SeriesAdapter
    private lateinit var VuduAdapter: VuduAdapter
    private lateinit var movieData: ArrayList<Title>
    private lateinit var seriesData: ArrayList<Title>
    private lateinit var netflixData: ArrayList<Title>
    private lateinit var vuduData: ArrayList<Title>

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private var netflix = false
    private var vudu = false
    private var series = false
    private var movie = false

    @DelicateCoroutinesApi
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        movieData = arrayListOf()
        seriesData = arrayListOf()
        netflixData = arrayListOf()
        vuduData = arrayListOf()

        SeriesAdapter = SeriesAdapter(seriesData,requireContext())
        NetflixAdapter = NetflixAdapter(netflixData,requireContext())
        VuduAdapter = VuduAdapter(vuduData,requireContext())
        MovieAdapter = MovieListAdapter(movieData, requireContext())

        val SerieslayoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        val NetflixlayoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        val VudulayoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        val MovielayoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)

        binding.upcomingSeries.layoutManager = SerieslayoutManager
        binding.upcomingSeries.adapter = SeriesAdapter

        binding.upcomingNetflix.layoutManager = NetflixlayoutManager
        binding.upcomingNetflix.adapter = NetflixAdapter

        binding.upcomingVudu.layoutManager = VudulayoutManager
        binding.upcomingVudu.adapter = VuduAdapter

        binding.upcomingMovies.layoutManager = MovielayoutManager
        binding.upcomingMovies.adapter = MovieAdapter

        makeInvisible()
        getSeries()
        getNetflix()
        getVudu()
        getMovie()

        return binding.root
    }

    @DelicateCoroutinesApi
    private fun getSeries() {
        val movieApi = RetrofitHelper.getInstance().create(watchmodeAPI::class.java)
        GlobalScope.launch(Dispatchers.IO) {
            val result = movieApi.getWatchList(API_KEY, 0,"popularity_desc","tv_series",1,10)
            val movieDataResult = result.body()
            if (movieDataResult != null) {
                seriesData.addAll(movieDataResult.titles)
                withContext(Dispatchers.Main) {
                    SeriesAdapter.updateData(seriesData)
                    series = true
                    visibilityChecker()
                }
            }
        }
    }
    @DelicateCoroutinesApi
    private fun getNetflix() {
        val movieApi = RetrofitHelper.getInstance().create(watchmodeAPI::class.java)
        GlobalScope.launch(Dispatchers.IO) {
            val result = movieApi.getWatchList(API_KEY, NETFLIX,"popularity_desc","",1,10)
            val movieDataResult = result.body()
            if (movieDataResult != null) {
                netflixData.addAll(movieDataResult.titles)
                withContext(Dispatchers.Main) {
                    NetflixAdapter.updateData(netflixData)
                    netflix = true
                    visibilityChecker()
                }
            }
        }
    }
    @DelicateCoroutinesApi
    private fun getVudu() {
        val movieApi = RetrofitHelper.getInstance().create(watchmodeAPI::class.java)
        GlobalScope.launch(Dispatchers.IO) {
            val result = movieApi.getWatchList(API_KEY, VUDU,"popularity_desc","",1,10)
            val movieDataResult = result.body()
            if (movieDataResult != null) {
                vuduData.addAll(movieDataResult.titles)
                withContext(Dispatchers.Main) {
                    VuduAdapter.updateData(vuduData)
                    vudu = true
                    visibilityChecker()
                }
            }
        }
    }
    @DelicateCoroutinesApi
    private fun getMovie() {
        val movieApi = RetrofitHelper.getInstance().create(watchmodeAPI::class.java)
        GlobalScope.launch(Dispatchers.IO) {
            val result = movieApi.getWatchList(API_KEY, 0,"popularity_desc","movie",1,10)
            val movieDataResult = result.body()
            if (movieDataResult != null) {
                movieData.addAll(movieDataResult.titles)
                withContext(Dispatchers.Main) {
                    MovieAdapter.updateData(movieData)
                    movie = true
                    visibilityChecker()
                }
            }
        }
    }

    fun makeInvisible(){
        binding.txtmovie.visibility = View.INVISIBLE
        binding.txtnetflix.visibility = View.INVISIBLE
        binding.txtseries.visibility = View.INVISIBLE
        binding.txtvudu.visibility = View.INVISIBLE

        binding.upcomingSeries.visibility = View.INVISIBLE
        binding.upcomingVudu.visibility = View.INVISIBLE
        binding.upcomingMovies.visibility = View.INVISIBLE
        binding.upcomingNetflix.visibility = View.INVISIBLE
    }
    fun visibilityChecker(){
        if(movie && series && vudu && movie){
            binding.txtmovie.visibility = View.VISIBLE
            binding.txtnetflix.visibility = View.VISIBLE
            binding.txtseries.visibility = View.VISIBLE
            binding.txtvudu.visibility = View.VISIBLE

            binding.upcomingSeries.visibility = View.VISIBLE
            binding.upcomingVudu.visibility = View.VISIBLE
            binding.upcomingMovies.visibility = View.VISIBLE
            binding.upcomingNetflix.visibility = View.VISIBLE
            binding.animationView.visibility = View.INVISIBLE
        }
    }
}