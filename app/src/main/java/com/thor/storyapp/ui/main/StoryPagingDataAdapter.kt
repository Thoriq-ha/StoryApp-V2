package com.thor.storyapp.ui.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.thor.storyapp.R
import com.thor.storyapp.data.local.story.story_entity.StorySchema
import com.thor.storyapp.databinding.ItemStoryBinding

class StoryPagingDataAdapter(private val itemClickListener: (StorySchema) -> Unit) :
    PagingDataAdapter<StorySchema, MyViewHolder>(
        COMPARATOR
    ) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
//        val binding = ItemStoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return MyViewHolder.create(parent)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val data = getItem(position)
        if (data != null) {
            holder.bind(data)
            holder.binding.root.setOnClickListener {
                itemClickListener.invoke(data)
            }
        }
    }


    companion object {
        private val COMPARATOR = object : DiffUtil.ItemCallback<StorySchema>() {
            override fun areItemsTheSame(oldItem: StorySchema, newItem: StorySchema): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: StorySchema, newItem: StorySchema): Boolean {
                return oldItem == newItem
            }
        }
    }
}


class MyViewHolder(val binding: ItemStoryBinding) : RecyclerView.ViewHolder(binding.root) {

    fun bind(data: StorySchema) {
        binding.info.text = data.createdAt
        binding.name.text = data.name

        Glide.with(binding.root.context)
            .load(data.photoUrl)
            .placeholder(R.mipmap.ic_launcher)
            .centerCrop()
            .into(binding.avatar)

        Glide.with(binding.root.context)
            .load(data.photoUrl)
            .placeholder(R.mipmap.ic_launcher)
            .centerCrop()
            .into(binding.imageView)
    }

    companion object {
        fun create(parent: ViewGroup): MyViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_story, parent, false)

            val binding = ItemStoryBinding.bind(view)

            return MyViewHolder(
                binding
            )
        }
    }
}


//
//    PagingDataAdapter<StorySchema, StoryPagingDataAdapter.MyViewHolder>(DIFF_CALLBACK) {
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
//        val binding = ItemStoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
//        return MyViewHolder(binding)
//    }
//
//    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
//        val data = getItem(position)
//        if (data != null) {
//            holder.bind(data)
//            holder.binding.root.setOnClickListener {
//                itemClickListener.invoke(data)
//            }
//        }
//    }
//
//    class MyViewHolder(val binding: ItemStoryBinding) :
//        RecyclerView.ViewHolder(binding.root) {
//        fun bind(data: StorySchema) {
//            binding.info.text = data.createdAt
//            binding.name.text = data.name
//
//            Glide.with(binding.root.context)
//                .load(data.photoUrl)
//                .placeholder(R.mipmap.ic_launcher)
//                .centerCrop()
//                .into(binding.avatar)
//
//            Glide.with(binding.root.context)
//                .load(data.photoUrl)
//                .placeholder(R.mipmap.ic_launcher)
//                .centerCrop()
//                .into(binding.imageView)
//        }
//    }
//
//    companion object {
//        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<StorySchema>() {
//            override fun areItemsTheSame(oldItem: StorySchema, newItem: StorySchema): Boolean {
//                return oldItem == newItem
//            }
//
//            override fun areContentsTheSame(oldItem: StorySchema, newItem: StorySchema): Boolean {
//                return oldItem.id == newItem.id
//            }
//        }
//    }
//}