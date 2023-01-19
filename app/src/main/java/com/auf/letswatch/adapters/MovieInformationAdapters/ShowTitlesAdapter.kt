package com.auf.letswatch.adapters.MovieInformationAdapters

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.auf.letswatch.databinding.ContentMoviesourcesViewBinding
import com.auf.letswatch.models.informations.Source
import com.bumptech.glide.Glide
import java.util.*
import kotlin.collections.ArrayList

class ShowTitlesAdapter(private var title: ArrayList<Source>, private var context: Context): RecyclerView.Adapter<ShowTitlesAdapter.MovieViewHolder>()  {
    inner class MovieViewHolder(private val binding: ContentMoviesourcesViewBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(moviedata: Source) {
            binding.name.text = moviedata.name
            val soures = moviedata.name.lowercase(Locale.ROOT).replace(' ','_')
            Log.e("",soures)
            Glide.with(binding.root)
                .load("https://cdn.watchmode.com/provider_logos/"+soures+"_100px"+".png")
                .override(binding.imglogo.width,binding.imglogo.height)
                .into(binding.imglogo)
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val binding = ContentMoviesourcesViewBinding.inflate(LayoutInflater.from(parent.context),parent, false)
        return MovieViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        val moviedata = title[position]
        holder.bind(moviedata)
    }

    override fun getItemCount(): Int {
        return title.size
    }

    fun updateData(movieDataList: ArrayList<Source>){
        this.title = arrayListOf()
        notifyDataSetChanged()
        this.title = movieDataList
        this.notifyItemInserted(this.title.size)
    }
}