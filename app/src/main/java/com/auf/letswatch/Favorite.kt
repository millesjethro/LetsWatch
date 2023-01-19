package com.auf.letswatch

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.auf.letswatch.adapters.FavoriteAdapters.MovieDataBaseAdapter
import com.auf.letswatch.databinding.FragmentFavoriteBinding
import com.auf.letswatch.models.MovieDB.MovieDataBase
import com.auf.letswatch.services.realm.config.RealmConfig
import com.auf.letswatch.services.realm.operation.Operations
import io.realm.Realm
import io.realm.RealmConfiguration
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext


class Favorite : Fragment(), MovieDataBaseAdapter.MovieDBAdaptersInterface {
    private var _binding: FragmentFavoriteBinding? = null
    private val binding get() = _binding!!
    private lateinit var MovieDataBase: ArrayList<MovieDataBase>
    private lateinit var adapter: MovieDataBaseAdapter
    private lateinit var config: RealmConfiguration
    private lateinit var operation: Operations
    private lateinit var coroutine: CoroutineContext


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onResume() {
        super.onResume()

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFavoriteBinding.inflate(inflater, container, false)
        MovieDataBase = arrayListOf()

        adapter = MovieDataBaseAdapter(MovieDataBase,requireContext(),this)

        val layoutManager: RecyclerView.LayoutManager = GridLayoutManager(context,3,GridLayoutManager.VERTICAL,false)
        binding.favoriteRv.layoutManager = layoutManager
        binding.favoriteRv.adapter = adapter
        binding.favoriteRv.visibility = View.INVISIBLE
        Realm.init(requireContext())
        config = RealmConfig.getConfiguration()
        operation = Operations(config)
        coroutine = Job() + Dispatchers.IO
        getMovie()

        return binding.root
    }


    private fun getMovie(){
        val sharedPreferences = this.activity?.getSharedPreferences("MY_PROFILE", Context.MODE_PRIVATE)
        val username = sharedPreferences?.getString(USERNAME_KEY,"")
        val scope = CoroutineScope(coroutine)
        scope.launch(Dispatchers.IO){
            withContext(Dispatchers.Main){
                val result = operation.retrieveSpecificUser(username.toString())
                withContext(Dispatchers.Main){
                    adapter.updateData(result as ArrayList<MovieDataBase>)
                    binding.favoriteRv.visibility = View.VISIBLE
                    binding.animationView.visibility = View.INVISIBLE
                }
            }
        }
    }

    override fun removeFav(id: String) {
        val scope = CoroutineScope(coroutine)
        scope.launch (Dispatchers.IO) {
            operation.removeMovie(id)
            withContext(Dispatchers.Main){
                getMovie()
            }
        }
    }

}