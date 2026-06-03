package id.editor.newsapp.ui.page

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import id.editor.newsapp.data.repository.Result
import id.editor.newsapp.databinding.FragmentPageListBinding

class PageListFragment : Fragment() {

    private var _binding: FragmentPageListBinding? = null
    private val binding get() = _binding!!

    private val viewModel: PageViewModel by viewModels()
    private lateinit var pageAdapter: PageAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPageListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        setupSwipeRefresh()
        observeViewModel()

        if (savedInstanceState == null) {
            viewModel.fetchPages()
        }
    }

    private fun setupRecyclerView() {
        pageAdapter = PageAdapter { page ->
            val action = PageListFragmentDirections.actionPageListToPageDetail(page.id)
            findNavController().navigate(action)
        }
        binding.pagesRecyclerView.adapter = pageAdapter
    }

    private fun setupSwipeRefresh() {
        binding.swipeRefresh.setOnRefreshListener {
            viewModel.fetchPages()
        }
    }

    private fun observeViewModel() {
        viewModel.pages.observe(viewLifecycleOwner) { result ->
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
                    val pages = result.data
                    pageAdapter.submitList(pages)
                    binding.emptyView.visibility = if (pages.isEmpty()) View.VISIBLE else View.GONE
                }
                is Result.Error -> {
                    binding.progressBar.visibility = View.GONE
                    binding.swipeRefresh.isRefreshing = false
                    binding.emptyView.visibility = if (pageAdapter.itemCount == 0) View.VISIBLE else View.GONE
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
