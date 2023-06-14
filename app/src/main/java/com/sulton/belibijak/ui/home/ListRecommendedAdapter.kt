package com.sulton.belibijak.ui.home

import android.view.LayoutInflater
import android.view.RoundedCorner
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.sulton.belibijak.data.remote.ItemsItem
import com.sulton.belibijak.databinding.ItemRecommendedBinding

class ListRecommendedAdapter(private val list: List<ItemsItem>) : RecyclerView.Adapter<ListRecommendedAdapter.ViewHolder>() {
    inner class ViewHolder(itemView: ItemRecommendedBinding) : RecyclerView.ViewHolder(itemView.root)
    {
        private val binding = itemView
        fun bind(data: ItemsItem){
            with(binding){
                Glide.with(itemView)
                    .load(data.avatarUrl)
                    .centerCrop()
                    .transform(RoundedCorners(10))
                    .into(imageView)
                tvRtitle.text = data.login
                tvPrice.text = data.id.toString()
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = ItemRecommendedBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
       holder.bind(list[position])
    }


}