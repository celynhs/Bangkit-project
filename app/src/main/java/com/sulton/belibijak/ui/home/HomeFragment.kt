package com.sulton.belibijak.ui.home


import android.content.Intent
import android.os.Bundle
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.sulton.belibijak.R
import com.sulton.belibijak.data.local.UserPreference
import com.sulton.belibijak.data.slideData
import com.sulton.belibijak.databinding.FragmentHomeBinding
import com.sulton.belibijak.ui.auth.AuthActivity
import com.sulton.belibijak.ui.auth.LoginActivity
import com.sulton.belibijak.ui.auth.LoginViewModel
import com.sulton.belibijak.ui.auth.ViewModelFactory
import kotlin.concurrent.fixedRateTimer

class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: HomeViewModel
    private lateinit var logViewModel: LoginViewModel
    private lateinit var adapter: ImageSliderAdapter
    private lateinit var rAdapter: ListRecommendedAdapter
    private val list = ArrayList<slideData>()
    private lateinit var dots: ArrayList<TextView>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentHomeBinding.bind(view)

        val pref = UserPreference.getInstance(requireContext())
        logViewModel = ViewModelProvider(this, ViewModelFactory(pref))[LoginViewModel::class.java]

        viewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory())[HomeViewModel::class.java]
        viewModel.searchUser("Sulton")
        binding.apply {
            rvRecommended.setHasFixedSize(true)
            rvRecommended.layoutManager = GridLayoutManager(requireActivity(), 2)
        }
        addDataList()
        addDataCarousel()

        adapter = ImageSliderAdapter(list)
        binding.viewPager2.adapter = adapter
        dots = ArrayList()
        setIndicator()

        binding.viewPager2.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback(){
            override fun onPageSelected(position: Int) {
                selectedSlide(position)
                super.onPageSelected(position)
            }
        })

        binding.ivUser.setOnClickListener {
          logViewModel.logOut()
            val intent = Intent(requireContext(), AuthActivity::class.java)
            startActivity(intent)
            requireActivity().finish()
        }

        with(binding){
            Glide.with(this@HomeFragment)
                .load(R.drawable.gojokun)
                .circleCrop()
                .into(ivUser)
        }
    }

    private fun selectedSlide(position: Int) {
    for (i in 0 until list.size){
        if (i == position)
            dots[i].setTextColor(ContextCompat.getColor(requireContext(), R.color.color_schema1))
        else
            dots[i].setTextColor(ContextCompat.getColor(requireContext(), R.color.color_schema2))
    }
    }

    private fun setIndicator() {
        for(i in 0 until  list.size)
        {
            dots.add(TextView(requireContext()))
            dots[i].text = Html.fromHtml("&#9679", Html.FROM_HTML_MODE_LEGACY).toString()
            dots[i].textSize = 18f
            binding.dotsIndicator.addView(dots[i])
        }
    }
    private fun addDataCarousel(){
        list.add(
            slideData("Fresh Product","Always provide best quality ingredients","https://drive.google.com/uc?export=view&id=1KH779VrqcsAKzynF-Zx2UIIDQtT7_3u8")
        )
        list.add(
            slideData("Best Sale","","")
        )
        list.add(
            slideData("Good fo Health","","")
        )
    }
    private fun addDataList(){
        viewModel.listUsers.observe(viewLifecycleOwner){
            if (it!=null){
                rAdapter = ListRecommendedAdapter(it)
                binding.rvRecommended.adapter = rAdapter
            }
        }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}