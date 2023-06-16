package com.sulton.belibijak.ui.home

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.sulton.belibijak.data.local.DataRekomend
import com.sulton.belibijak.databinding.ItemRecommendedBinding
import com.sulton.belibijak.ui.detail.DetailActivity

class ListRecommendedAdapter(private val listItem : List<DataRekomend>) : RecyclerView.Adapter<ListRecommendedAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: ItemRecommendedBinding) : RecyclerView.ViewHolder(itemView.root)
    {
        private val binding = itemView
        fun bind(data: DataRekomend){
            val total =data.recomended?.total
            val format = "Rp. $total"
            val listRecommendItem = data.recomended?.recommendations?.get(0)
            with(binding){
                Glide.with(itemView)
                    .load(data.imageUrl)
                    .centerCrop()
                    .transform(RoundedCorners(10))
                    .into(imageView)
                tvRtitle.text = data.title
                tvPrice.text = format
                btAdd.setOnClickListener {
                    val intent = Intent(itemView.context, DetailActivity::class.java)
                    intent.putExtra("Image", data.imageUrl)
                    intent.putExtra("Total", total.toString())
                    intent.putExtra("ItemCount", data.recomended?.recommendations?.size.toString())
                    intent.putExtra("Recommend", data.recomended?.recommendations.toString())
                    intent.putExtra("Title", data.title)
                itemView.context.startActivity(intent)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = ItemRecommendedBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = listItem.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
       holder.bind(listItem[position])
    }


}