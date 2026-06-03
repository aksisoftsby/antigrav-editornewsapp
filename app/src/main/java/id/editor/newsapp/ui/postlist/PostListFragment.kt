package id.editor.newsapp.ui.postlist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import id.editor.newsapp.data.repository.Result
import id.editor.newsapp.databinding.FragmentPostListBinding

class PostListFragment : Fragment() {

    private var _binding: FragmentPostListBinding? = null
    private val binding get() = _binding!!

    private val viewModel: PostListViewModel by viewModels()
    private val args: PostListFragmentArgs by navArgs()
    private lateinit var postAdapter: PostAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPostListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupTitle()
        setupRecyclerView()
        setupSwipeRefresh()
        observeViewModel()

        if (savedInstanceState == null) {
            viewModel.loadPosts(categoryId = args.categoryId, tagId = args.tagId, reset = true)
        }
    }

    private fun setupTitle() {
        val title = when {
            args.categoryName.isNotEmpty() -> args.categoryName
            args.tagName.isNotEmpty() -> "#${args.tagName}"
            else -> "Artikel"
        }
        (activity as? AppCompatActivity)?.supportActionBar?.title = title
    }

    private fun setupRecyclerView() {
        postAdapter = PostAdapter { post ->
            val action = PostListFragmentDirections.actionPostListToPostDetail(post.id)
            findNavController().navigate(action)
        }
        binding.postsRecyclerView.adapter = postAdapter

        // Scroll listener for pagination (infinite scroll)
        binding.postsRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                
                // Only trigger when scrolling down
                if (dy > 0) {
                    val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                    val visibleItemCount = layoutManager.childCount
                    val totalItemCount = layoutManager.itemCount
                    val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()

                    // Pre-fetch when user is 5 items away from the bottom of the list
                    if ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount - 5
                        && firstVisibleItemPosition >= 0
                    ) {
                        viewModel.loadPosts(categoryId = args.categoryId, tagId = args.tagId, reset = false)
                    }
                }
            }
        })
    }

    private fun setupSwipeRefresh() {
        binding.swipeRefresh.setOnRefreshListener {
            viewModel.loadPosts(categoryId = args.categoryId, tagId = args.tagId, reset = true)
        }
    }

    private fun observeViewModel() {
        viewModel.postsState.observe(viewLifecycleOwner) { result ->
            when (result) {
                is Result.Loading -> {
                    if (postAdapter.itemCount == 0) {
                        binding.progressBar.visibility = View.VISIBLE
                    } else {
                        binding.loadMoreProgress.visibility = View.VISIBLE
                    }
                    binding.emptyView.visibility = View.GONE
                }
                is Result.Success -> {
                    binding.progressBar.visibility = View.GONE
                    binding.loadMoreProgress.visibility = View.GONE
                    binding.swipeRefresh.isRefreshing = false
                    val posts = result.data.first
                    postAdapter.submitList(posts)
                    binding.emptyView.visibility = if (posts.isEmpty()) View.VISIBLE else View.GONE
                }
                is Result.Error -> {
                    binding.progressBar.visibility = View.GONE
                    binding.loadMoreProgress.visibility = View.GONE
                    binding.swipeRefresh.isRefreshing = false
                    binding.emptyView.visibility = if (postAdapter.itemCount == 0) View.VISIBLE else View.GONE
                    Toast.makeText(context, result.message, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
