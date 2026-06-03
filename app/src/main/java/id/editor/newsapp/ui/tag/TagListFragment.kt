package id.editor.newsapp.ui.tag

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import id.editor.newsapp.data.repository.Result
import id.editor.newsapp.databinding.FragmentTagListBinding

class TagListFragment : Fragment() {

    private var _binding: FragmentTagListBinding? = null
    private val binding get() = _binding!!

    private val viewModel: TagViewModel by viewModels()
    private lateinit var tagAdapter: TagAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTagListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        setupSwipeRefresh()
        observeViewModel()
    }

    private fun setupRecyclerView() {
        tagAdapter = TagAdapter { tag ->
            val action = TagListFragmentDirections.actionTagListToPostList(
                tagId = tag.id,
                tagName = tag.name
            )
            findNavController().navigate(action)
        }
        binding.tagsRecyclerView.layoutManager = StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL)
        binding.tagsRecyclerView.adapter = tagAdapter
    }

    private fun setupSwipeRefresh() {
        binding.swipeRefresh.setOnRefreshListener {
            viewModel.fetchTags()
        }
    }

    private fun observeViewModel() {
        viewModel.tags.observe(viewLifecycleOwner) { result ->
            when (result) {
                is Result.Loading -> {
                    if (!binding.swipeRefresh.isRefreshing) {
                        binding.progressBar.visibility = View.VISIBLE
                    }
                    binding.emptyView.visibility = View.GONE
                }
                is Result.Success -> {
                    binding.progressBar.visibility = View.GONE
                    binding.swipeRefresh.isRefreshing = false
                    val tags = result.data
                    tagAdapter.submitList(tags)
                    binding.emptyView.visibility = if (tags.isEmpty()) View.VISIBLE else View.GONE
                }
                is Result.Error -> {
                    binding.progressBar.visibility = View.GONE
                    binding.swipeRefresh.isRefreshing = false
                    binding.emptyView.visibility = if (tagAdapter.itemCount == 0) View.VISIBLE else View.GONE
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
