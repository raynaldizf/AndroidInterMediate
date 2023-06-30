package com.app.storyapp.adapter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.app.storyapp.databinding.ListStoryBinding
import com.app.storyapp.model.request.Story
import com.bumptech.glide.Glide

class HomeAdapter (private val dataStory : List<Story>) : RecyclerView.Adapter<HomeAdapter.ViewHolder>() {
    class ViewHolder(val binding : ListStoryBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = ListStoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return dataStory.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.textNama.text = dataStory[position].name
        holder.binding.textDescription.text = dataStory[position].description
        Glide.with(holder.itemView.context).load(dataStory[position].photoUrl).into(holder.binding.image)
        holder.binding.card.setOnClickListener{
            val bundle = Bundle()
            bundle.putString("id", dataStory[position].id)
            bundle.putString("name", dataStory[position].name)
            bundle.putString("description", dataStory[position].description)
            bundle.putString("photoUrl", dataStory[position].photoUrl)
            bundle.putString("createdAt", dataStory[position].createdAt)
            bundle.putDouble("lat", dataStory[position].lat)
            bundle.putDouble("lon", dataStory[position].lon)
            Navigation.findNavController(it).navigate(com.app.storyapp.R.id.action_homeFragment_to_detailFragment, bundle)
        }
    }
}