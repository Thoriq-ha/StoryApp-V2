package com.thor.storyapp.ui.main.detail

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.thor.storyapp.R
import com.thor.storyapp.databinding.FragmentDetailBinding
import com.thor.storyapp.utils.viewBinding

class DetailFragment : Fragment(R.layout.fragment_detail) {

    private val binding by viewBinding(FragmentDetailBinding::bind)

    private val story by lazy {
        DetailFragmentArgs.fromBundle(requireArguments()).story
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.info.text = story.createdAt
        binding.name.text = story.name
        binding.description.text = story.description

        Glide.with(binding.root.context)
            .load(story.photoUrl)
            .placeholder(R.mipmap.ic_launcher)
            .centerCrop()
            .into(binding.avatar)

        Glide.with(binding.root.context)
            .load(story.photoUrl)
            .placeholder(R.mipmap.ic_launcher)
            .fitCenter()
            .into(binding.imageView)

        if (story.lat != null && story.lon != null) {
            binding.btnLocation.setOnClickListener {
                findNavController().navigate(
                    DetailFragmentDirections.actionDetailFragmentToLocationStory(story)
                )
            }
        } else {
            binding.btnLocation.visibility = View.GONE
        }
    }
}