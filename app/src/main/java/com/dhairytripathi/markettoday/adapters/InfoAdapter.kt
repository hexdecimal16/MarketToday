package com.dhairytripathi.markettoday.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.dhairytripathi.markettoday.data.model.Info
import com.dhairytripathi.markettoday.databinding.ItemInfoBinding

class InfoAdapter : ListAdapter<Info, InfoAdapter.InfoViewHolder>(DiffCallback()){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InfoViewHolder {
        val binding = ItemInfoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return InfoViewHolder(binding)
    }

    override fun onBindViewHolder(holder: InfoViewHolder, position: Int) {
        val currentItem = getItem(position)
        holder.bind(currentItem)
    }


    class InfoViewHolder(private val binding: ItemInfoBinding) :
        RecyclerView.ViewHolder(binding.root) {
        init {
            binding.apply {
            }
        }

        fun bind(info: Info) {
            binding.apply {
                textViewPrice.text = String.format("%.2f", info.price)
                textViewChange.text = String.format("%.2f", info.change)
                textViewName.text = info.name

            }
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<Info>() {
        override fun areItemsTheSame(oldItem: Info, newItem: Info) =
            oldItem.name == newItem.name

        override fun areContentsTheSame(oldItem: Info, newItem: Info) =
            oldItem == newItem
    }
}