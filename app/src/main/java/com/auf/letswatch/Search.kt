package com.auf.letswatch

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.auf.letswatch.adapters.SearchAdapters.SearchListAdapter
import com.auf.letswatch.adapters.SearchAdapters.TitleListAdapter
import com.auf.letswatch.constants.API_KEY
import com.auf.letswatch.constants.TITLE_ID
import com.auf.letswatch.databinding.FragmentSearchBinding
import com.auf.letswatch.models.list.Title
import com.auf.letswatch.models.search.Result
import com.auf.letswatch.services.helper.RetrofitHelper
import com.auf.letswatch.services.repository.watchmodeAPI
import kotlinx.coroutines.*

class Search : Fragment() {
    private lateinit var searchAdapter: SearchListAdapter
    private lateinit var TitleAdapter: TitleListAdapter
    private lateinit var searchData: ArrayList<Result>
    private lateinit var movieData: ArrayList<Title>
    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }
    @DelicateCoroutinesApi
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)

        searchData = arrayListOf()
        movieData = arrayListOf()
        searchAdapter = SearchListAdapter(searchData,requireContext())
        TitleAdapter = TitleListAdapter(movieData,requireContext())

        binding.listrv.visibility = View.INVISIBLE

        val GlayoutManager = GridLayoutManager(context, 3, GridLayoutManager.VERTICAL,false)
        binding.listrv.layoutManager = GlayoutManager
        binding.listrv.adapter = TitleAdapter
        getTitles()

        binding.searchListTitle.addTextChangedListener(object : TextWatcher {

            override fun afterTextChanged(s: Editable) {

            }

            override fun beforeTextChanged(s: CharSequence, start: Int,
                                           count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence, start: Int,
                                       before: Int, count: Int) {
                val LlayoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL,false)
                if(binding.searchListTitle.text.toString() != "") {
                    binding.listrv.layoutManager = LlayoutManager
                    binding.listrv.adapter = searchAdapter
                    getSearch(binding.searchListTitle.text.toString())
                }
                else{
                    binding.listrv.layoutManager = LlayoutManager
                    binding.listrv.adapter = TitleAdapter
                    getTitles()
                }
            }
        })
        return binding.root
    }
    @DelicateCoroutinesApi
    private fun getSearch(searches: String) {
        val searchApi = RetrofitHelper.getInstance().create(watchmodeAPI::class.java)
        GlobalScope.launch(Dispatchers.IO) {
            val result = searchApi.getSearchList(API_KEY,searches,2)
            val searchDataResult = result.body()
            if (searchDataResult != null) {
                searchData.clear()
                searchData.addAll(searchDataResult.results)
                withContext(Dispatchers.Main) {
                    searchAdapter.updateData(searchData)
                    binding.listrv.visibility = View.VISIBLE
                    binding.animationView.visibility = View.INVISIBLE
                    Log.e("", TITLE_ID)
                }
            }
        }
    }
    @DelicateCoroutinesApi
    private fun getTitles() {
        val movieApi = RetrofitHelper.getInstance().create(watchmodeAPI::class.java)
        GlobalScope.launch(Dispatchers.IO) {
            val result = movieApi.getWatchList(API_KEY, 0,"popularity_desc","",1,250)
            val movieDataResult = result.body()
            if (movieDataResult != null) {
                searchData.clear()
                movieData.clear()
                movieData.addAll(movieDataResult.titles)
                withContext(Dispatchers.Main) {
                    TitleAdapter.updateData(movieData)
                    binding.listrv.visibility = View.VISIBLE
                    binding.animationView.visibility = View.INVISIBLE
                }
            }
        }
    }


}