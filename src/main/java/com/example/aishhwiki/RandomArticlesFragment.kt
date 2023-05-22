package com.example.aishhwiki

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.aishhwiki.databinding.FragmentRandomArticlesBinding

class RandomArticlesFragment: Fragment() {
    private lateinit var binding: FragmentRandomArticlesBinding
    private lateinit var randomArticlesAdapter: RandomArticlesAdapter
    private lateinit var landingViewModel: LandingViewModel
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentRandomArticlesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        randomArticlesAdapter = RandomArticlesAdapter()

        binding.randomArticlesRecyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = randomArticlesAdapter
        }


        landingViewModel = ViewModelProvider(requireActivity()).get(LandingViewModel::class.java)
        landingViewModel.randomArticles.observe(viewLifecycleOwner, Observer { randomArticles ->
            randomArticlesAdapter.submitList(randomArticles)
        })


        landingViewModel.fetchRandomArticles()
    }
}