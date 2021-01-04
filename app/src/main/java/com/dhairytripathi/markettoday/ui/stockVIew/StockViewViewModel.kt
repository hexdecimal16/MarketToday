package com.dhairytripathi.markettoday.ui.stockVIew

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.dhairytripathi.markettoday.data.model.History
import com.dhairytripathi.markettoday.data.model.Info
import com.dhairytripathi.markettoday.data.model.Stock
import com.github.mikephil.charting.data.Entry
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow

class StockViewViewModel @ViewModelInject constructor(
    @Assisted private val state: SavedStateHandle
) : ViewModel() {

    val stock = state.get<Stock>("stock")
    val list : MutableList<Info> = arrayListOf()
    val listLive : MutableLiveData<List<Info>> = MutableLiveData()
    var lineSet: MutableLiveData<List<Entry>> = MutableLiveData()
    var lineSetList: MutableList<Entry> = arrayListOf()
    private val stockViewChannel = Channel<StockViewEvent>()
    val stockViewEvent = stockViewChannel.receiveAsFlow()

    fun generateGraph(data: MutableList<History>) {
        var his = data[0]
        for (history in data) {
            his = history
            lineSetList.add( Entry(
                history.timestamp.toFloat() * 1000,
                String.format("%.2f", history.open).toFloat()
            ))
        }
        lineSet.apply {
            value = lineSetList
        }
        list.add(Info(his.high, "High", his.change))
        list.add(Info(his.low, "Low", his.change))
        list.add(Info(his.currentPrice, "Price", his.change))
        list.add(Info(his.open, "Open", his.change))
        listLive.apply {
            value = list
        }
    }

    sealed class StockViewEvent {
        object NavigateBack : StockViewEvent()
        data class NavigateBackWithResult(val result: Int) : StockViewEvent()
    }

}