package com.dhairytripathi.markettoday.ui.news

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.dhairytripathi.markettoday.ui.ViewState
import com.dhairytripathi.markettoday.utils.observeNotNull
import com.dhairytripathi.markettoday.R
import com.dhairytripathi.markettoday.adapters.NewsArticlesAdapter
import com.dhairytripathi.markettoday.databinding.FragmentNewsBinding
import dagger.hilt.android.AndroidEntryPoint

private const val TAG = "NewsFragment"
@AndroidEntryPoint
class NewsFragment : Fragment(R.layout.fragment_news){
    private val newsViewModel: NewsViewModel by viewModels()

    private lateinit var binding: FragmentNewsBinding


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentNewsBinding.bind(view )
        newsViewModel.logNews()
        val adapter = NewsArticlesAdapter { newsViewModel.openArticle() }
        binding.newsList.adapter = adapter
        binding.newsList.layoutManager = LinearLayoutManager(context)
        newsViewModel.getNewsArticles().observeNotNull(this) {state ->
            when(state) {
                is ViewState.Success -> adapter.submitList(state.data)
                is ViewState.Loading -> binding.newsList.showLoading()
                is ViewState.Error -> Toast.makeText(context,"Something went wrong ¯\\_(ツ)_/¯ => ${state.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
