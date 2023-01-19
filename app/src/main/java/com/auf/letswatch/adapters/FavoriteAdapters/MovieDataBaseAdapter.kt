package com.auf.letswatch.adapters.FavoriteAdapters

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import com.auf.letswatch.databinding.ContentListViewBinding
import com.auf.letswatch.models.MovieDB.MovieDataBase
import com.auf.letswatch.movietitle
import com.bumptech.glide.Glide
import java.io.Serializable

class MovieDataBaseAdapter(private var movieList: ArrayList<MovieDataBase>, private var context: Context, private var favorite: MovieDBAdaptersInterface): RecyclerView.Adapter<MovieDataBaseAdapter.MovieDBHolders>(),
    Serializable {

    interface MovieDBAdaptersInterface{
        fun removeFav(id:String)
    }

    inner class MovieDBHolders(private val binding: ContentListViewBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(movieData: MovieDataBase){
            Glide.with(binding.root)
                .load(movieData.poster)
                .into(binding.imgposter)
            itemView.setOnClickListener{
                val activity = context as FragmentActivity
                val fm: FragmentManager = activity.supportFragmentManager
                val DialogFragment = movietitle()
                val bundle = Bundle()
                bundle.putString("titleID", movieData.id)
                DialogFragment.arguments = bundle
                DialogFragment.show(fm, "fragment_dialog")
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieDBHolders {
        val binding = ContentListViewBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return MovieDBHolders(binding)
    }

    override fun onBindViewHolder(holder: MovieDBHolders, position: Int) {
        val movieData = movieList[position]
        holder.bind(movieData)
    }

    override fun getItemCount(): Int {
        return movieList.size
    }

    fun updateData(movielist: ArrayList<MovieDataBase>){
        this.movieList = arrayListOf()
        notifyDataSetChanged()
        this.movieList.addAll(movielist)
        this.notifyItemInserted(this.movieList.size)
    }

}