package id.editor.newsapp.ui.tag

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import id.editor.newsapp.R
import id.editor.newsapp.data.model.Tag
import id.editor.newsapp.databinding.ItemTagBinding

class TagAdapter(private val onTagClick: (Tag) -> Unit) :
    ListAdapter<Tag, TagAdapter.TagViewHolder>(TagDiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TagViewHolder {
        val binding = ItemTagBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return TagViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TagViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class TagViewHolder(private val binding: ItemTagBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.tagChip.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    onTagClick(getItem(position))
                }
            }
        }

        fun bind(tag: Tag) {
            binding.tagChip.text = "#${tag.name} (${tag.count})"
        }
    }

    companion object {
        private val TagDiffCallback = object : DiffUtil.ItemCallback<Tag>() {
            override fun areItemsTheSame(oldItem: Tag, newItem: Tag): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Tag, newItem: Tag): Boolean {
                return oldItem == newItem
            }
        }
    }
}
