package id.editor.newsapp.ui.page

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import id.editor.newsapp.data.model.Page
import id.editor.newsapp.databinding.ItemPageBinding
import org.jsoup.Jsoup

class PageAdapter(private val onPageClick: (Page) -> Unit) :
    ListAdapter<Page, PageAdapter.PageViewHolder>(PageDiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PageViewHolder {
        val binding = ItemPageBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return PageViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PageViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class PageViewHolder(private val binding: ItemPageBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    onPageClick(getItem(position))
                }
            }
        }

        fun bind(page: Page) {
            val cleanTitle = Jsoup.parse(page.title.rendered).text()
            binding.pageTitle.text = cleanTitle
        }
    }

    companion object {
        private val PageDiffCallback = object : DiffUtil.ItemCallback<Page>() {
            override fun areItemsTheSame(oldItem: Page, newItem: Page): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Page, newItem: Page): Boolean {
                return oldItem == newItem
            }
        }
    }
}
