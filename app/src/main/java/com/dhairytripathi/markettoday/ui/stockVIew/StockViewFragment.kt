package com.dhairytripathi.markettoday.ui.stockVIew

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dhairytripathi.markettoday.R
import com.dhairytripathi.markettoday.adapters.InfoAdapter
import com.dhairytripathi.markettoday.databinding.FragmentStockViewBinding
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis.AxisDependency
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect


private const val TAG = "StockViewFragment"
@AndroidEntryPoint
class StockViewFragment : Fragment(R.layout.fragment_stock_view) {

    private  val stockViewViewModel: StockViewViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val drawableColor: Drawable = ColorDrawable(Color.WHITE)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = FragmentStockViewBinding.bind(view)
        val infoAdapter = InfoAdapter()

        binding.apply {
            textViewStockName.text = stockViewViewModel.stock?.name
            textViewStockSymbol.text = stockViewViewModel.stock?.symbol
            textViewStockCurrentPrice.text = stockViewViewModel.stock?.currentPrice.toString()
            textViewStockChange.text = stockViewViewModel.stock?.currentChange.toString()
            stockViewViewModel.generateGraph(stockViewViewModel.stock?.data!!)
            stockViewViewModel.lineSet.observe(viewLifecycleOwner) {
                if (chart.data != null &&
                    chart.data.dataSetCount > 0) {
                    var data = chart.data.getDataSetByIndex(0) as LineDataSet
                    data.values = it
                    chart.notifyDataSetChanged()
                } else {
                    var set2 = LineDataSet(it, "Price")
                    set2.axisDependency = AxisDependency.RIGHT
                    set2.color = Color.RED
                    set2.setDrawCircles(false)
                    set2.setDrawValues(false)
                    set2.mode = LineDataSet.Mode.CUBIC_BEZIER
                    set2.lineWidth = 2f
                    set2.circleRadius = 3f
                    set2.fillAlpha = 65
                    set2.fillColor = Color.RED
                    set2.setDrawCircleHole(false)
                    set2.highLightColor = Color.rgb(244, 117, 117)
                    val data = LineData(set2)
                    data.setValueTextColor(Color.WHITE)
                    data.setValueTextSize(9f)
                    chart.data = data
                }
                val xAxisFormatter = HourAxisValueFormatter()
                val xAxis: XAxis = chart.xAxis
                xAxis.valueFormatter = xAxisFormatter
            }
            recyclerViewList.apply {
                adapter = infoAdapter
                layoutManager = LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false)
                setHasFixedSize(true)
            }
            stockViewViewModel.listLive.observe(viewLifecycleOwner) {
                infoAdapter.submitList(it)
            }
        }

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            stockViewViewModel.stockViewEvent.collect { value: StockViewViewModel.StockViewEvent ->
                when(value) {
                    is StockViewViewModel.StockViewEvent.NavigateBackWithResult -> {
                        binding.chart.clearFocus()
                        binding.textViewStockCurrentPrice.clearFocus()
                        binding.textViewStockSymbol.clearFocus()
                        binding.constraintLayout.clearFocus()
                        binding.recyclerViewList.clearFocus()
                        binding.textViewStockChange.clearFocus()
                        binding.textViewStockName.clearFocus()
                        setFragmentResult(
                                "home_fragment",
                                bundleOf("home_fragment_result" to value)
                        )
                        findNavController().popBackStack()
                    }
                }
            }
        }


    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
    }

}