package id.editor.newsapp.ui.postdetail

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import id.editor.newsapp.R
import id.editor.newsapp.data.model.Post
import id.editor.newsapp.data.repository.Result
import id.editor.newsapp.databinding.FragmentPostDetailBinding
import org.jsoup.Jsoup

class PostDetailFragment : Fragment() {

    private var _binding: FragmentPostDetailBinding? = null
    private val binding get() = _binding!!

    private val viewModel: PostDetailViewModel by viewModels()
    private val args: PostDetailFragmentArgs by navArgs()
    private var currentPost: Post? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPostDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupMenu()
        observeViewModel()

        if (savedInstanceState == null) {
            viewModel.loadPost(args.postId)
        }
    }

    private fun setupMenu() {
        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menu.clear()
                menuInflater.inflate(R.menu.post_detail_menu, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return when (menuItem.itemId) {
                    R.id.action_share -> {
                        currentPost?.let { sharePost(it) }
                        true
                    }
                    R.id.action_browser -> {
                        currentPost?.let { openInBrowser(it.link) }
                        true
                    }
                    else -> false
                }
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }

    private fun observeViewModel() {
        viewModel.post.observe(viewLifecycleOwner) { result ->
            when (result) {
                is Result.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                }
                is Result.Success -> {
                    binding.progressBar.visibility = View.GONE
                    val post = result.data
                    currentPost = post
                    displayPost(post)
                }
                is Result.Error -> {
                    binding.progressBar.visibility = View.GONE
                    Toast.makeText(context, result.message, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun displayPost(post: Post) {
        val cleanTitle = Jsoup.parse(post.title.rendered).text()
        binding.detailTitle.text = cleanTitle
        binding.authorName.text = post.getAuthorName()

        val rawDate = post.date
        val formattedDate = formatDate(rawDate)
        binding.postDate.text = formattedDate

        val featuredImage = post.getFeaturedImageUrl()
        if (!featuredImage.isNullOrEmpty()) {
            Glide.with(this)
                .load(featuredImage)
                .placeholder(R.drawable.gradient_overlay)
                .error(R.drawable.gradient_overlay)
                .into(binding.detailImage)
            binding.detailImage.visibility = View.VISIBLE
        } else {
            binding.detailImage.visibility = View.GONE
        }

        val authorAvatar = post.getAuthorAvatarUrl()
        if (!authorAvatar.isNullOrEmpty()) {
            Glide.with(this)
                .load(authorAvatar)
                .placeholder(R.drawable.ic_contact)
                .into(binding.authorAvatar)
        }

        val webSettings = binding.detailWebView.settings
        webSettings.javaScriptEnabled = true
        webSettings.domStorageEnabled = true

        val isDarkMode = (resources.configuration.uiMode and android.content.res.Configuration.UI_MODE_NIGHT_MASK) == android.content.res.Configuration.UI_MODE_NIGHT_YES
        val bgColor = if (isDarkMode) "#1A1C1E" else "#FFFFFF"
        val textColor = if (isDarkMode) "#E3E2E6" else "#1C1B1F"
        val linkColor = if (isDarkMode) "#4DB6AC" else "#00796B"

        val htmlData = """
            <html>
            <head>
            <meta name="viewport" content="width=device-width, initial-scale=1.0">
            <style>
                body {
                    background-color: $bgColor;
                    color: $textColor;
                    font-family: sans-serif;
                    line-height: 1.6;
                    margin: 0;
                    padding: 0;
                    font-size: 16px;
                }
                img {
                    max-width: 100%;
                    height: auto;
                    border-radius: 8px;
                    margin: 12px 0;
                }
                a {
                    color: $linkColor;
                    text-decoration: none;
                }
                iframe {
                    max-width: 100%;
                    height: 200px;
                    border-radius: 8px;
                    margin: 12px 0;
                }
                blockquote {
                    border-left: 4px solid $linkColor;
                    margin: 16px 0;
                    padding-left: 16px;
                    color: #79747E;
                    font-style: italic;
                }
            </style>
            </head>
            <body>
                ${post.content.rendered}
            </body>
            </html>
        """.trimIndent()

        binding.detailWebView.loadDataWithBaseURL(null, htmlData, "text/html", "UTF-8", null)
    }

    private fun sharePost(post: Post) {
        val cleanTitle = Jsoup.parse(post.title.rendered).text()
        val shareIntent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_SUBJECT, cleanTitle)
            putExtra(Intent.EXTRA_TEXT, "$cleanTitle\n\n${post.link}")
        }
        startActivity(Intent.createChooser(shareIntent, getString(R.string.share_post)))
    }

    private fun openInBrowser(url: String) {
        try {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            startActivity(intent)
        } catch (e: Exception) {
            Toast.makeText(context, "Cannot open link", Toast.LENGTH_SHORT).show()
        }
    }

    private fun formatDate(rawDate: String): String {
        return try {
            val parser = java.text.SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", java.util.Locale.getDefault())
            val formatter = java.text.SimpleDateFormat("dd MMM yyyy", java.util.Locale.getDefault())
            val date = parser.parse(rawDate)
            if (date != null) formatter.format(date) else rawDate
        } catch (e: Exception) {
            rawDate
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
