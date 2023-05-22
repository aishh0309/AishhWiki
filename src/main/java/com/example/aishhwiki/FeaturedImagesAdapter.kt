package com.example.aishhwiki

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide


class FeaturedImagesAdapter(private var featuredImages: MutableList<String> = mutableListOf()) :
    RecyclerView.Adapter<FeaturedImagesAdapter.ImageViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_image, parent, false)
        return ImageViewHolder(view)
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        val featuredImage = featuredImages[position]
        holder.bind(featuredImage)
    }

    override fun getItemCount(): Int = featuredImages.size
    fun submitList(newfeaturedImages: List<String>) {
        featuredImages.addAll(newfeaturedImages)
        notifyDataSetChanged()
    }

    inner class ImageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val imageView: ImageView = itemView.findViewById(R.id.imageView)

        fun bind(featuredImage: String) {
           // itemView.findViewById<TextView>(R.id.categoryNameTextView).text = featuredImage.title
            // Load and display the image using an image loading library like Glide or Picasso
            Glide.with(itemView.context)
                .load(featuredImage)
               // .override(200, 200)
                .into(imageView)
        }
    }
}