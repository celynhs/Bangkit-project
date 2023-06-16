package com.sulton.belibijak.ui.cart

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.sulton.belibijak.data.local.UserPreference
import com.sulton.belibijak.data.local.dataOrder
import com.sulton.belibijak.databinding.FragmentCartBinding
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class CartFragment : Fragment() {
    private var _binding: FragmentCartBinding? = null
    private val binding get() = _binding!!
    private var order: dataOrder? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments !=null){
            arguments?.let {
                order = it.getParcelable("order")
            }
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentCartBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val price = order?.rp?.toDouble()
        val formatData = String.format("%.2f", price)
        val pricetv = "Rp. $formatData"

        val tax : Double = 30000.00
        val total = java.lang.StringBuilder()
        total.append("Rp. ")
        val totald = price?.plus(tax)
        val formatTotal = String.format("%.2f", totald)
        total.append(formatTotal)

        val pref = UserPreference.getInstance(requireContext())

        setSummary(pref, totald)

        with(binding){
            Glide.with(this@CartFragment)
                .load(order?.img)
                .centerCrop()
                .into(ivProduk)

           tvPrice.text = pricetv
           tvPaketName.text = order?.nama
            tvPcs.text = order?.pcs
            tvSubtotal.text = pricetv
            tvBagFee.text="Rp. 0"
            tvTax.text = "Rp. $tax"
            tvTotalR.text = total
        }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    fun setSummary(userPreference: UserPreference, totald: Double?){
        lifecycleScope.launch {
            val budget = java.lang.StringBuilder()
            val bAwal = userPreference.getBudget().first()
            val formatData = String.format("%.2f", bAwal)
            budget.append("Rp. ")
            budget.append(formatData)
            binding.tvBudgetLimit.text = budget
            var orderTotal = 0.0
            if (totald != null) {
                orderTotal = totald
            }
            val sisa = String.format("%.2f", bAwal-orderTotal)
            val last = "Rp. $sisa"
            binding.tvBudgetEnd.text = last
        }

    }
}