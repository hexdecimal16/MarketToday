package com.dhairytripathi.markettoday.ui.home

import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.navGraphViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dhairytripathi.markettoday.adapters.StocksAdapter
import com.dhairytripathi.markettoday.data.model.Stock
import com.dhairytripathi.markettoday.utils.onQueryTextChanged
import com.dhairytripathi.markettoday.R
import com.dhairytripathi.markettoday.databinding.FragmentHomeBinding
import com.dhairytripathi.markettoday.utils.exhaustive
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect

private const val TAG = "HomeFragment"

@AndroidEntryPoint
class HomeFragment : Fragment(R.layout.fragment_home), StocksAdapter.OnItemClickListener {

    val homeViewModel: HomeViewModel by navGraphViewModels(R.id.mobile_navigation) {
        defaultViewModelProviderFactory
    }
    private lateinit var binding : FragmentHomeBinding;
    private lateinit var searchView: SearchView

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentHomeBinding.bind(view)
        val stocksAdapter = StocksAdapter(this)
        homeViewModel.getAllStocks()
        binding.apply {
            recyclerViewStocks.apply {
                adapter = stocksAdapter
                layoutManager = LinearLayoutManager(requireContext())
                setHasFixedSize(true)
            }
            recyclerViewStocks.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    val layoutManager = recyclerViewStocks.layoutManager as LinearLayoutManager
                    val pos = layoutManager.findLastCompletelyVisibleItemPosition()
                    val numItems: Int = stocksAdapter.itemCount
                    if (pos >= numItems - 1 && numItems > 15) {
                        homeViewModel.loadNextPage()
                    }
                }
            })

            textViewAllStocks.setOnClickListener {
                setBackground(1)
            }
            textViewTopGain.setOnClickListener {
                setBackground(2)
            }
            textViewTopLose.setOnClickListener {
                setBackground(3)
            }
        }


        homeViewModel.allStocks.observe(viewLifecycleOwner) {
            stocksAdapter.notifyDataSetChanged()
            stocksAdapter.submitList(it)
        }

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            homeViewModel.stocksEvent.collect { event ->
                when(event) {
                    is HomeViewModel.StocksEvent.NavigateToStockView -> {
                        val action =
                            HomeFragmentDirections.actionHomeFragmentToStockViewFragment(
                                event.stock
                            )
                        findNavController().navigate(action)
                    }
                }.exhaustive
            }
        }
        homeViewModel.loading.observe(viewLifecycleOwner) {
            if(it) {
                binding.progressBar.visibility = View.VISIBLE
            } else {
                binding.progressBar.visibility = View.GONE
            }
        }
        setHasOptionsMenu(true)
    }

    private fun removeBackground() {
        binding.apply {
            textViewAllStocks.background = context?.let { ContextCompat.getDrawable(it, R.drawable.bg_transparent) }
            textViewTopGain.background = context?.let { ContextCompat.getDrawable(it, R.drawable.bg_transparent) }
            textViewTopLose.background = context?.let { ContextCompat.getDrawable(it, R.drawable.bg_transparent) }
        }
    }

    private fun setBackground(pos: Int) {
        removeBackground()
        if(pos == 1) {
            binding.textViewAllStocks.background = context?.let { ContextCompat.getDrawable(it, R.drawable.highlight_color) }
        }
        if(pos == 2) {
            binding.textViewTopGain.background = context?.let { ContextCompat.getDrawable(it, R.drawable.highlight_color) }
            homeViewModel.setTopGainers()
        }
        if(pos == 3) {
            binding.textViewTopLose.background = context?.let { ContextCompat.getDrawable(it, R.drawable.highlight_color) }
            homeViewModel.setTopLosers()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.top_action_menu, menu)
        val searchItem = menu.findItem(R.id.action_search)
        searchView = searchItem.actionView as SearchView

        val pendingQuery = homeViewModel.searchQuery.value
        if(pendingQuery != null && pendingQuery.isNotEmpty()) {
            searchItem.expandActionView()
            searchView.setQuery(pendingQuery, false)
        }

        searchView.onQueryTextChanged {
            Log.d(TAG, "onCreateOptionsMenu: $it")
            homeViewModel.searchQuery.value = it
            homeViewModel.filter()
        }
    }

    override fun onItemClick(stock: Stock) {
        homeViewModel.onStockSelected(stock)
    }
}