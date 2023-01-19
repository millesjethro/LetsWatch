package com.auf.letswatch

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.auf.letswatch.adapters.ListAdapters.ListsAdapters
import com.auf.letswatch.constants.*
import com.auf.letswatch.databinding.FragmentListBinding
import com.auf.letswatch.models.list.Title
import com.auf.letswatch.services.helper.RetrofitHelper
import com.auf.letswatch.services.repository.watchmodeAPI
import kotlinx.coroutines.*
import java.util.*
import kotlin.collections.ArrayList


class List : Fragment(), AdapterView.OnItemSelectedListener, View.OnClickListener {

    private lateinit var TitleAdapter: ListsAdapters
    private lateinit var movieData: ArrayList<Title>

    private var _binding: FragmentListBinding? = null
    private val binding get() = _binding!!
    private var type: String = ""
    private var provider: Int = 0
    private var pagenumber: Int = 1
    private var totalpage: Int = 0

    @DelicateCoroutinesApi
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentListBinding.inflate(inflater, container, false)

        movieData = arrayListOf()

        TitleAdapter = ListsAdapters(movieData,requireContext())
        val layoutManager = GridLayoutManager(context, 3, GridLayoutManager.VERTICAL,false)
        binding.list.layoutManager = layoutManager
        binding.list.adapter = TitleAdapter
        binding.list.visibility = View.INVISIBLE

        val hash = hashMapOf<String, Any>(
            "Netflix" to NETFLIX,
            "Vudu" to VUDU,
            "Youtube" to YOUTUBE,
            "Amazon" to AMAZON,
            "iTunes" to ITUNES,
            "Hulu" to HULU)

        val selectedMovieType = binding.MovieType.selectedItem.toString()
        type = selectedMovieType.lowercase(Locale.ROOT).replace(' ','_')
        val selectedMovieProvider = binding.MovieProvider.selectedItem.toString()
        provider = hash[selectedMovieProvider] as Int


        Log.e("", type)
        Log.e("", provider.toString())

        getTitles()
        binding.btnPrevpage.visibility = View.INVISIBLE
        binding.MovieType.onItemSelectedListener = this
        binding.MovieProvider.onItemSelectedListener = this
        binding.btnPrevpage.setOnClickListener(this)
        binding.btnNextpage.setOnClickListener(this)

        return binding.root
    }
    @DelicateCoroutinesApi
    private fun getTitles() {
        val movieApi = RetrofitHelper.getInstance().create(watchmodeAPI::class.java)
        GlobalScope.launch(Dispatchers.IO) {
            val result = movieApi.getWatchList(API_KEY, provider,"popularity_desc",type,pagenumber,30)
            val movieDataResult = result.body()
            if (movieDataResult != null) {
                movieData.clear()
                movieData.addAll(movieDataResult.titles)
                withContext(Dispatchers.Main) {
                    TitleAdapter.updateData(movieData)
                    binding.animationView.visibility = View.INVISIBLE
                    binding.list.visibility = View.VISIBLE
                    totalpage = movieDataResult.total_pages
                }
            }
        }
    }

    @DelicateCoroutinesApi
    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
        when(p0!!.id){
            (R.id.MovieProvider)->{
                val selectedMovieProvider = binding.MovieProvider.selectedItem.toString()
                val hash = hashMapOf<String, Any>(
                    "Netflix" to NETFLIX,
                    "Vudu" to VUDU,
                    "Youtube" to YOUTUBE,
                    "Amazon" to AMAZON,
                    "iTunes" to ITUNES,
                    "Hulu" to HULU)
                provider = hash[selectedMovieProvider] as Int
                binding.animationView.visibility = View.VISIBLE
                binding.list.visibility = View.INVISIBLE
                binding.list.scrollToPosition(0);
                pagenumber = 1
                getTitles()

            }
            (R.id.MovieType)->{
                val selectedMovieType = binding.MovieType.selectedItem.toString()
                type = selectedMovieType.lowercase(Locale.ROOT).replace(' ','_')
                binding.animationView.visibility = View.VISIBLE
                binding.list.visibility = View.INVISIBLE
                binding.list.scrollToPosition(0);
                pagenumber = 1
                getTitles()
            }
        }
    }
    override fun onNothingSelected(p0: AdapterView<*>?) {

    }

    @DelicateCoroutinesApi
    override fun onClick(p0: View?) {
        when(p0!!.id){
            (R.id.btn_prevpage)->{
                pagenumber -= 1

                if(pagenumber>1) {
                    binding.btnNextpage.visibility = View.VISIBLE
                }
                else{
                    pagenumber = 1
                    binding.btnPrevpage.visibility = View.INVISIBLE
                }
                binding.txtPagenumber.text = pagenumber.toString()
                binding.animationView.visibility = View.VISIBLE
                binding.list.visibility = View.INVISIBLE
                getTitles()
            }
            (R.id.btn_nextpage)->{
                pagenumber++
                if(pagenumber>0){
                    binding.btnPrevpage.visibility = View.VISIBLE
                }
                else if(pagenumber == totalpage){
                    binding.btnNextpage.visibility = View.INVISIBLE
                }
                binding.txtPagenumber.text = pagenumber.toString()
                binding.animationView.visibility = View.VISIBLE
                binding.list.visibility = View.INVISIBLE
                getTitles()
            }
        }
    }


}