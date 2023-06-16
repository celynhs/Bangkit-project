package com.sulton.belibijak.ui.cart

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.sulton.belibijak.R
import com.sulton.belibijak.databinding.FragmentCartBinding
import com.sulton.belibijak.databinding.FragmentHomeBinding

class CartFragment : Fragment() {
    private var _binding: FragmentCartBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: CartViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {



        _binding = FragmentCartBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if(arguments != null){

            val bundle = arguments
            // Extract the data using appropriate keys
            val value1 = bundle?.getString("paket")
            val value2 = bundle?.getString("price")
            val value3 = bundle?.getString("pcs")

            binding.tvPrice.text = value2
            binding.tvPaketName.text = value1
            binding.tvPcs.text = value3

        }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    companion object {
        fun newInstance(paket: String, price: String, pcs: String): CartFragment {
            val fragment = CartFragment()
            val bundle = Bundle().apply {
                putString("paket", paket)
                putString("price", price)
                putString("pcs", pcs)
            }
            fragment.arguments = bundle
            return fragment
        }
    }
}