package com.sulton.belibijak.ui.detail

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.sulton.belibijak.MainActivity
import com.sulton.belibijak.R
import com.sulton.belibijak.data.local.UserPreference
import com.sulton.belibijak.databinding.ActivityDetailBinding
import com.sulton.belibijak.ui.auth.LoginViewModel
import com.sulton.belibijak.ui.home.RecommendViewModel
import com.sulton.belibijak.ui.home.StoryModelFactory
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding
    private lateinit var adapter: DetailAdapter
    private val recViewModel: RecommendViewModel by viewModels {
        StoryModelFactory(this)
    }
    private lateinit var loginViewModel: LoginViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.closeButton.setOnClickListener {
            super.onBackPressed()
        }


        val pref = UserPreference.getInstance(this)
        binding.recyclerView2.layoutManager = LinearLayoutManager(this)

        getDataIntent()
        getItemList(pref)
        val swipeHandler = object : SwipeToDeleteCallback(this) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val adapter = binding.recyclerView2.adapter as DetailAdapter
                adapter.removeAt(viewHolder.adapterPosition)
                val pcsl = "${adapter.itemCount} Pcs"
                binding.textPcsDetail.text = pcsl
                val total = String.format("%.2f",adapter.sumPrice())
                val Price = "Rp. $total"
                binding.textPriceDetail.text = Price
            }
        }
        val itemTouchHelper = ItemTouchHelper(swipeHandler)
        itemTouchHelper.attachToRecyclerView(binding.recyclerView2)


        binding.goCheckOut.setOnClickListener {

            val imgurl = intent.getStringExtra("Image")
            val intent = Intent(this, MainActivity::class.java)

            val adapter = binding.recyclerView2.adapter as DetailAdapter

            intent.putExtra("cart", R.id.navigation_cart)
            intent.putExtra("Title", binding.tvDetailTittle.text.toString())
            intent.putExtra("Price", adapter.sumPrice().toString())
            intent.putExtra("Pcs", binding.textPcsDetail.text.toString())
            intent.putExtra("Img", imgurl)
            startActivity(intent)
        }
    }
    fun getDataIntent(){
        val imgurl = intent.getStringExtra("Image")
        with(binding){
            Glide.with(this@DetailActivity)
                .load(imgurl)
                .into(bundleImg)
        }

    }
    private fun getItemList(userPreference: UserPreference){
        lifecycleScope.launch {
            val budget = userPreference.getBudget().first()

            recViewModel.getRecommendItem(budget)
            recViewModel.loading.observe(this@DetailActivity){
                showLoading(it)
            }
            recViewModel.recomndedList.observe(this@DetailActivity){

                adapter = DetailAdapter(it!!.toMutableList())
                binding.recyclerView2.adapter = adapter
                val pcs = "${it.size} Pcs"
                val fromatPrice = String.format("%.2f",adapter.sumPrice())
                val rp  = "Rp. $fromatPrice"
                binding.textPcsDetail.text = pcs
                binding.textPriceDetail.text = rp
            }
        }
    }
    private fun showLoading(isLoading: Boolean) {
        binding.progressBar4.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

}