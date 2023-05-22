package com.example.aishhwiki

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.aishhwiki.databinding.FragmentFeaturedImagesBinding

class FeaturedImagesFragment:Fragment() {
    private lateinit var binding: FragmentFeaturedImagesBinding
    private lateinit var featuredImagesAdapter: FeaturedImagesAdapter
    private lateinit var landingViewModel: LandingViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentFeaturedImagesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        featuredImagesAdapter = FeaturedImagesAdapter()

        binding.featuredImagesRecyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = featuredImagesAdapter
        }
        landingViewModel = ViewModelProvider(requireActivity()).get(LandingViewModel::class.java)
        landingViewModel.featuredImages.observe(viewLifecycleOwner, Observer { featuredImages ->
            featuredImagesAdapter.submitList(featuredImages)
        })

        landingViewModel.fetchFeaturedImages()
    }

}