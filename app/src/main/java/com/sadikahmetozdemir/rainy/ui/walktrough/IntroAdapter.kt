package com.sadikahmetozdemir.rainy.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.sadikahmetozdemir.rainy.core.shared.remote.IntroModel
import com.sadikahmetozdemir.rainy.databinding.ItemViewpagerBinding

class IntroAdapter(val list: ArrayList<IntroModel>) : RecyclerView.Adapter<IntroViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IntroViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return IntroViewHolder(ItemViewpagerBinding.inflate(inflater, parent, false))
    }

    override fun onBindViewHolder(holder: IntroViewHolder, position: Int) {
        holder.bind(list[position])
    }

    override fun getItemCount(): Int {
        return list.size
    }
}

class IntroViewHolder(val binding: ItemViewpagerBinding) : RecyclerView.ViewHolder(binding.root) {
    fun bind(item: IntroModel) {
        binding.apply {
            item.backgroundId?.let { cLayout.setBackgroundResource(it) }
            ivViewpager.setImageDrawable(item.drawableId?.let {
                ContextCompat.getDrawable(
                    binding.root.context,
                    it
                )
            })
            tvViewpagerTitle.text = item.tittle
            tvDescription.text = item.description
        }
    }
}