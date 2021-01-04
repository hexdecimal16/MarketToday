package com.dhairytripathi.markettoday.adapters

import android.content.Intent
import android.net.Uri
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.dhairytripathi.markettoday.data.model.NewsAdapterEvent
import com.dhairytripathi.markettoday.data.storage.entity.NewsArticleDb
import com.dhairytripathi.markettoday.utils.inflate
import com.dhairytripathi.markettoday.R
import com.dhairytripathi.markettoday.databinding.ItemNewsBinding
import java.text.SimpleDateFormat
import java.util.*


/**
 * The News adapter to show the news in a list.
 */
class NewsArticlesAdapter(
        private val listener: (NewsAdapterEvent) -> Unit
) : ListAdapter<NewsArticleDb, NewsArticlesAdapter.NewsHolder>(DIFF_CALLBACK) {

    /**
     * Inflate the view
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = NewsHolder(parent.inflate(R.layout.item_news))

    /**
     * Bind the view with the data
     */
    override fun onBindViewHolder(newsHolder: NewsHolder, position: Int) = newsHolder.bind(getItem(position), listener)

    /**
     * View Holder Pattern
     */
    class NewsHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val binding = ItemNewsBinding.bind(itemView)

        /**
         * Binds the UI with the data and handles clicks
         */
        fun bind(newsArticle: NewsArticleDb, listener: (NewsAdapterEvent) -> Unit) = with(itemView) {
            binding.textViewTitle.text = newsArticle.title
            binding.textViewSource.text = newsArticle.author
            binding.textViewDesc.text = newsArticle.description
            binding.textViewTime.text = getFormattedDate(newsArticle.publishedAt)
            binding.imageViewNewsPic.load(newsArticle.urlToImage)
            setOnClickListener {
                val i = Intent(Intent.ACTION_VIEW)
                i.data = Uri.parse(newsArticle.url)
                context.startActivity(i)
            }
        }

        private fun getFormattedDate(publishedAt: String?): CharSequence? {
            val publishedTimestamp = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'").parse(publishedAt).time
            val currentTimestamp = Date().time
            var difference = currentTimestamp - publishedTimestamp
            difference /= 1000
            return when {
                difference < 3600 -> {
                    "${difference/60} minutes ago"
                }
                difference in 3600..86399 -> {
                    "${difference / 3600} hours ago"
                }
                else -> {
                    if (difference/86400 == 1L) {
                        "${difference/86400} day ago"
                    } else {
                        "${difference/86400} days ago"
                    }
                }
            }
        }
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<NewsArticleDb>() {
            override fun areItemsTheSame(oldItem: NewsArticleDb, newItem: NewsArticleDb): Boolean = oldItem.id == newItem.id
            override fun areContentsTheSame(oldItem: NewsArticleDb, newItem: NewsArticleDb): Boolean = oldItem == newItem
        }
    }
    /**
     * Format date to the difference
     */

}
