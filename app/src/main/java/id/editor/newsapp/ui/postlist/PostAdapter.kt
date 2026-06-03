package id.editor.newsapp.ui.postlist

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import id.editor.newsapp.R
import id.editor.newsapp.data.model.Post
import id.editor.newsapp.databinding.ItemPostBinding
import org.jsoup.Jsoup

class PostAdapter(private val onPostClick: (Post) -> Unit) :
    ListAdapter<Post, PostAdapter.PostViewHolder>(PostDiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val binding = ItemPostBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return PostViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class PostViewHolder(private val binding: ItemPostBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    onPostClick(getItem(position))
                }
            }
        }

        fun bind(post: Post) {
            // Clean title and excerpt HTML
            val cleanTitle = Jsoup.parse(post.title.rendered).text()
            val cleanExcerpt = Jsoup.parse(post.excerpt.rendered).text()

            binding.postTitle.text = cleanTitle
            binding.postExcerpt.text = cleanExcerpt

            // Set categories
            val categories = post.getCategoryNames()
            if (categories.isNotEmpty()) {
                binding.postCategory.text = categories.first()
                binding.postCategory.visibility = android.view.View.VISIBLE
            } else {
                binding.postCategory.visibility = android.view.View.GONE
            }

            // Set date & author meta
            val rawDate = post.date
            val formattedDate = formatDate(rawDate)
            binding.postMeta.text = binding.root.context.getString(
                R.string.by_author,
                post.getAuthorName()
            ) + " • " + formattedDate

            // Load featured image
            val imageUrl = post.getFeaturedImageUrl()
            if (!imageUrl.isNullOrEmpty()) {
                Glide.with(binding.postImage.context)
                    .load(imageUrl)
                    .placeholder(R.drawable.gradient_overlay)
                    .error(R.drawable.gradient_overlay)
                    .into(binding.postImage)
                binding.postImage.visibility = android.view.View.VISIBLE
            } else {
                binding.postImage.visibility = android.view.View.GONE
            }
        }

        private fun formatDate(rawDate: String): String {
            // Simple date parser for ISO 8601 like "2026-06-03T11:15:05"
            return try {
                val parser = java.text.SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", java.util.Locale.getDefault())
                val formatter = java.text.SimpleDateFormat("dd MMM yyyy", java.util.Locale.getDefault())
                val date = parser.parse(rawDate)
                if (date != null) formatter.format(date) else rawDate
            } catch (e: Exception) {
                rawDate
            }
        }
    }

    companion object {
        private val PostDiffCallback = object : DiffUtil.ItemCallback<Post>() {
            override fun areItemsTheSame(oldItem: Post, newItem: Post): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Post, newItem: Post): Boolean {
                return oldItem == newItem
            }
        }
    }
}
