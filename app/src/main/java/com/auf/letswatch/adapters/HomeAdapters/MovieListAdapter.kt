package com.auf.letswatch.adapters.HomeAdapters

import com.auf.letswatch.R
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import com.auf.letswatch.constants.IMGURL
import com.auf.letswatch.databinding.ContentListViewBinding
import com.auf.letswatch.models.list.Title
import com.auf.letswatch.movietitle
import com.bumptech.glide.Glide

class MovieListAdapter(private var movielist: ArrayList<Title>, private var context: Context): RecyclerView.Adapter<MovieListAdapter.MovieViewHolder>() {

    inner class MovieViewHolder(private val binding: ContentListViewBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(moviedata: Title) {

            Glide.with(binding.root)
                .load(IMGURL+moviedata.id+"_poster_w185.jpg")
                .into(binding.imgposter)

            val aniSlideup = AnimationUtils.loadAnimation(context, R.anim.slide_up)
            binding.imgposter.startAnimation(aniSlideup)

            itemView.setOnClickListener{
                val activity = context as FragmentActivity
                val fm: FragmentManager = activity.supportFragmentManager
                val DialogFragment = movietitle()
                val bundle = Bundle()
                bundle.putString("titleID", moviedata.id.toString())
                DialogFragment.arguments = bundle;
                DialogFragment.show(fm, "fragment_dialog")
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val binding = ContentListViewBinding.inflate(LayoutInflater.from(parent.context),parent, false)
        return MovieViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        val moviedata = movielist[position]
        holder.bind(moviedata)
    }

    override fun getItemCount(): Int {
        return movielist.size
    }

    fun updateData(movieDataList: ArrayList<Title>){
        this.movielist = arrayListOf()
        notifyDataSetChanged()
        this.movielist = movieDataList
        this.notifyItemInserted(this.movielist.size)
    }
}