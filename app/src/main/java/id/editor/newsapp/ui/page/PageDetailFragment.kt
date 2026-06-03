package id.editor.newsapp.ui.page

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import id.editor.newsapp.data.model.Page
import id.editor.newsapp.data.repository.Result
import id.editor.newsapp.databinding.FragmentPageDetailBinding
import org.jsoup.Jsoup

class PageDetailFragment : Fragment() {

    private var _binding: FragmentPageDetailBinding? = null
    private val binding get() = _binding!!

    private val viewModel: PageViewModel by viewModels()
    private val args: PageDetailFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPageDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observeViewModel()

        if (savedInstanceState == null) {
            viewModel.fetchPageDetails(args.pageId)
        }
    }

    private fun observeViewModel() {
        viewModel.pageDetails.observe(viewLifecycleOwner) { result ->
            when (result) {
                is Result.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                }
                is Result.Success -> {
                    binding.progressBar.visibility = View.GONE
                    displayPage(result.data)
                }
                is Result.Error -> {
                    binding.progressBar.visibility = View.GONE
                    Toast.makeText(context, result.message, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun displayPage(page: Page) {
        val cleanTitle = Jsoup.parse(page.title.rendered).text()
        binding.pageDetailTitle.text = cleanTitle

        val webSettings = binding.pageDetailWebView.settings
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
            </style>
            </head>
            <body>
                ${page.content.rendered}
            </body>
            </html>
        """.trimIndent()

        binding.pageDetailWebView.loadDataWithBaseURL(null, htmlData, "text/html", "UTF-8", null)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
