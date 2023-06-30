package com.app.storyapp.adapter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.app.storyapp.databinding.ListStoryBinding
import com.app.storyapp.model.response.ListStoryItem
import com.bumptech.glide.Glide

class QuoteListAdapter : PagingDataAdapter<ListStoryItem, QuoteListAdapter.MyViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ListStoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val data = getItem(position)
        if (data != null) {
            holder.bind(data)
        }
    }

    class MyViewHolder(private val binding: ListStoryBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: ListStoryItem) {
            binding.textNama.text = data.name
            binding.textDescription.text = data.description
            Glide.with(itemView.context)
                .load(data.photoUrl)
                .into(binding.image)

            binding.card.setOnClickListener{
                val bundle = Bundle()
                bundle.putString("id", data.id)
                bundle.putString("name", data.name)
                bundle.putString("description", data.description)
                bundle.putString("photoUrl", data.photoUrl)
                bundle.putString("createdAt", data.createdAt)
                bundle.putDouble("lat", data.lat)
                bundle.putDouble("lon", data.lon)
                Navigation.findNavController(it).navigate(com.app.storyapp.R.id.action_homeFragment_to_detailFragment, bundle)
            }
        }
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ListStoryItem>() {
            override fun areItemsTheSame(oldItem: ListStoryItem, newItem: ListStoryItem): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: ListStoryItem, newItem: ListStoryItem): Boolean {
                return oldItem.id == newItem.id
            }
        }
    }
}