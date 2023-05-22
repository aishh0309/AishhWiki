package com.example.aishhwiki

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class TabPagerAdapter(activity: AppCompatActivity) : FragmentStateAdapter(activity) {
    override fun getItemCount(): Int {
        return 3
    }
    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> RandomArticlesFragment()
            1 -> FeaturedImagesFragment()
            2 -> CategoriesFragment()
            else -> throw IllegalArgumentException("Invalid tab position")
        }
    }
}