package com.example.aishhwiki

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class RandomArticlesAdapter(private var articles:MutableList<RandomArticle> = mutableListOf()) :
    RecyclerView.Adapter<RandomArticlesAdapter.ArticleViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticleViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_article, parent, false)
        return ArticleViewHolder(view)
    }

    override fun onBindViewHolder(holder: ArticleViewHolder, position: Int) {
        val article = articles[position]
        holder.bind(article)
    }

    override fun getItemCount(): Int = articles.size
    fun submitList(newArticles: List<RandomArticle>) {
        articles.addAll(newArticles)
        notifyDataSetChanged()
    }

    inner class ArticleViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val articleTitleTextView: TextView = itemView.findViewById(R.id.articleTitleTextView)

        fun bind(article: RandomArticle) {
            itemView.findViewById<TextView>(R.id.articleTitleTextView).text = article.title
            itemView.findViewById<TextView>(R.id.articleContentTextView).text = article.content
            // Load and display the image using an image loading library like Glide or Picasso
            Glide.with(itemView.context)
                .load(article.imageUrl)

                .into(itemView.findViewById<ImageView>(R.id.imageView))
        }
    }
}