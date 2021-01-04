package com.dhairytripathi.markettoday.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.dhairytripathi.markettoday.data.model.Stock
import com.dhairytripathi.markettoday.databinding.ItemStocksBinding

class StocksAdapter(private val listener: OnItemClickListener) : ListAdapter<Stock, StocksAdapter.StocksViewHolder>(
    DiffCallback()
) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StocksViewHolder {
        val binding = ItemStocksBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return StocksViewHolder(binding)
    }

    override fun onBindViewHolder(holder: StocksViewHolder, position: Int) {
        val currentItem = getItem(position)
        if (currentItem != null) {
            holder.bind(currentItem)
        }
    }


    inner class StocksViewHolder(private val binding: ItemStocksBinding) :
            RecyclerView.ViewHolder(binding.root) {
                init {
                    binding.apply {
                        root.setOnClickListener {
                            val position = adapterPosition
                            if(position != RecyclerView.NO_POSITION) {
                                val stock = getItem(position)
                                if (stock != null) {
                                    listener.onItemClick(stock)
                                }
                            }
                        }
                    }
                }

                fun bind(stock: Stock) {
                    binding.apply {
                        tvStockName.text = stock.name
                        tvStockSymbol.text = stock.symbol
                    }
                }
            }

    interface OnItemClickListener {
        fun onItemClick(stock: Stock)
    }


    class DiffCallback : DiffUtil.ItemCallback<Stock>() {
        override fun areItemsTheSame(oldItem: Stock, newItem: Stock) =
                oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: Stock, newItem: Stock) =
                oldItem == newItem
    }

}