package com.auf.letswatch.adapters.SearchAdapters

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import com.auf.letswatch.databinding.ContentSearchlistViewBinding
import com.auf.letswatch.models.search.Result
import com.auf.letswatch.movietitle
import com.bumptech.glide.Glide


class SearchListAdapter(private var searchList: ArrayList<Result>, private var context: Context): RecyclerView.Adapter<SearchListAdapter.SearchViewHolder>() {

    inner class SearchViewHolder(val binding: ContentSearchlistViewBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(searchData: Result){
            Glide.with(binding.root)
                .load(searchData.image_url)
                .into(binding.searchImg)
            binding.searchTitle.text = searchData.name
            itemView.setOnClickListener{
                val activity = context as FragmentActivity
                val fm: FragmentManager = activity.supportFragmentManager
                val DialogFragment = movietitle()
                val bundle = Bundle()
                bundle.putString("titleID", searchData.id.toString())
                DialogFragment.arguments = bundle;
                DialogFragment.show(fm, "fragment_dialog")
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchViewHolder {
        val binding = ContentSearchlistViewBinding.inflate(LayoutInflater.from(parent.context),parent, false)
        return SearchViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SearchViewHolder, position: Int) {
        val searchData = searchList[position]
        holder.bind(searchData)
    }

    override fun getItemCount(): Int {
        return searchList.size
    }

    fun updateData(SmovieDataList: ArrayList<Result>){
        this.searchList = arrayListOf()
        notifyDataSetChanged()
        this.searchList = SmovieDataList
        this.notifyItemInserted(this.searchList.size)
    }



}

