package com.dhairytripathi.markettoday.ui.home

import android.util.Log
import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dhairytripathi.markettoday.data.constants.companies
import com.dhairytripathi.markettoday.data.model.History
import com.dhairytripathi.markettoday.data.model.Stock
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

private const val TAG = "HomeViewModel"
class HomeViewModel @ViewModelInject constructor(@Assisted private val state: SavedStateHandle) : ViewModel() {



    val searchQuery = state.getLiveData("searchQuery", "")
    val loading: MutableLiveData<Boolean> = MutableLiveData(true)
    private val db : FirebaseFirestore = Firebase.firestore
    var allStocks: MutableLiveData<List<Stock>> = MutableLiveData()
    var page : Int = 1;
    private val stocksEventChannel = Channel<StocksEvent>()
    val stocksEvent = stocksEventChannel.receiveAsFlow()
    private val allStocksList: MutableList<Stock> = arrayListOf()


    fun getAllStocks() {
        for(company in companies) {
            if(company[0].toInt() > (page*25 - 25) && company[0].toInt() < page * 25) {
                db.collection("graph").document(company[1]).collection("history").get()
                    .addOnSuccessListener { result ->
                        if(!result.isEmpty) {
                            var currentPrice = 0f
                            var currentChange = 0f
                            var history: MutableList<History> = arrayListOf()
                            for (document in result) {
                                val arr: HashMap<* , *> = document.data["data"] as HashMap<*, *>
                                history.add(
                                    History(
                                        (arr["High"] as String).trim().toFloat(),
                                        (arr["Low"] as String).trim().toFloat(),
                                        (arr["Open"] as String).trim().toFloat(),
                                        (arr["c_price"] as String).trim().toFloat(),
                                        (arr["change"] as String).trim().toFloat(),
                                        (arr["p_change"] as String).trim().toFloat(),
                                        arr["timestamp"] as Long
                                    )
                                )
                                currentPrice = (arr["c_price"] as String).trim().toFloat()
                                currentChange = (arr["change"] as String).trim().toFloat()
                            }
                            allStocksList.add( (Stock(company[2], company[1] , 0, history, currentPrice, currentChange)) )
                        }
                    }
                    .continueWith {
                        allStocks.apply {
                            value = allStocksList
                        }
                        loading.apply {
                            value = false
                        }
                    }
            }

        }

    }


    fun filter() {
        loading.apply {
            value = true
        }
        val query: String = searchQuery.value.toString()
        val maxResult = 15
        var i = 0
        allStocksList.clear()
        allStocks.apply {
            value = allStocksList
        }
        if (query == "" || query.isEmpty()){
            page = 1
            getAllStocks()
        } else {
            for (company in companies) {
                if (query in company[2] && i < maxResult) {
                    i++;
                    Log.d(TAG, "filter: ${company[2]}")
                    db.collection("graph").document(company[1]).collection("history").get()
                            .addOnSuccessListener { result ->
                                if (!result.isEmpty) {
                                    var currentPrice = 0f
                                    var currentChange = 0f
                                    var history: MutableList<History> = arrayListOf()
                                    for (document in result) {
                                        val arr: HashMap<*, *> = document.data["data"] as HashMap<*, *>
                                        history.add(
                                                History(
                                                        (arr["High"] as String).trim().toFloat(),
                                                        (arr["Low"] as String).trim().toFloat(),
                                                        (arr["Open"] as String).trim().toFloat(),
                                                        (arr["c_price"] as String).trim().toFloat(),
                                                        (arr["change"] as String).trim().toFloat(),
                                                        (arr["p_change"] as String).trim().toFloat(),
                                                        arr["timestamp"] as Long
                                                )
                                        )
                                        currentPrice = (arr["c_price"] as String).trim().toFloat()
                                        currentChange = (arr["change"] as String).trim().toFloat()
                                    }
                                    allStocksList.add((Stock(company[2], company[1], 0, history, currentPrice, currentChange)))
                                }
                            }
                            .continueWith {
                                allStocks.apply {
                                    value = allStocksList
                                }
                                loading.apply {
                                    value = false
                                }
                            }
                }
            }
        }
    }
    fun onStockSelected(stock: Stock) = viewModelScope.launch {
        stocksEventChannel.send(StocksEvent.NavigateToStockView(stock))
    }

    fun loadNextPage() {
        loading.apply {
            value = true
        }
        page++
        getAllStocks()
    }

    fun setTopGainers() {
        loading.apply {
            value = true
        }
        allStocksList.sortBy {
            it.currentChange
        }
        allStocksList.reverse()
        allStocks.apply {
            value = allStocksList
        }
        loading.value = false
    }

    fun setTopLosers() {
        loading.apply {
            value = true
        }
        allStocksList.sortBy {
            it.currentChange
        }
        allStocks.apply {
            value = allStocksList
        }
        loading.value = false
    }
    sealed class StocksEvent {
        data class NavigateToStockView(val stock: Stock) : StocksEvent()
    }
}