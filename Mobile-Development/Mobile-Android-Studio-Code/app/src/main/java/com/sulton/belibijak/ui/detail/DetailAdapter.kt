package com.sulton.belibijak.ui.detail

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.sulton.belibijak.data.remote.RecommendationsItem
import com.sulton.belibijak.databinding.ItemDetailBinding
import com.sulton.belibijak.databinding.ItemRecommendedBinding

class DetailAdapter(private val List: MutableList<RecommendationsItem?>) : RecyclerView.Adapter<DetailAdapter.ViewHolder>() {
    inner class ViewHolder(itemView: ItemDetailBinding) : RecyclerView.ViewHolder(itemView.root){
        private val binding = itemView
        fun bind(data: RecommendationsItem){
            val price = data.discountPrice?.let { formatData(it) }
            val weight = data.weightedAvg?.let { formatData(it) }
            with(binding){
                txtNamaBarang.text = data.name
                txtBerat.text= weight
                txtPrice.text= price
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = ItemDetailBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = List.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        List[position]?.let { holder.bind(it) }

    }
    fun removeAt(position: Int) {
        List.removeAt(position)
        notifyItemRemoved(position)
        notifyItemRangeChanged(position, List.size)
    }

    fun sumPrice() : Double{
        var sum = 0.0
        for (data in List){
           sum += data?.discountPrice!!
        }
        return sum

    }

    fun formatData(double: Double) : String{
        return String.format("%.2f", double)
    }
}