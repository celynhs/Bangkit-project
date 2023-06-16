package com.sulton.belibijak.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.sulton.belibijak.data.slideData
import com.sulton.belibijak.databinding.ItemSlideBinding

class ImageSliderAdapter(private val list: List<slideData>) : RecyclerView.Adapter<ImageSliderAdapter.ImageViewHolder>() {
    inner class ImageViewHolder(itemView: ItemSlideBinding) : RecyclerView.ViewHolder(itemView.root){
        private val binding = itemView
        fun bind(data: slideData){
            with(binding){
                Glide.with(itemView)
                    .load(data.imgUrl)
                    .into(imageView3)
            }
            binding.tvTittleCr.text = data.title
            binding.tvDescCr.text = data.desc
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        val view = ItemSlideBinding.inflate(LayoutInflater.from(parent.context), parent, false)
       return ImageViewHolder(view)
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
       holder.bind(list[position])
    }
}