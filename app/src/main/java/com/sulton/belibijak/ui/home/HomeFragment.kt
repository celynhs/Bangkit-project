package com.sulton.belibijak.ui.home


import android.os.Bundle
import android.text.Html
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.sulton.belibijak.data.slideData
import com.sulton.belibijak.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: HomeViewModel
    private lateinit var adapter: ImageSliderAdapter
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
        viewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory())[HomeViewModel::class.java]

        list.add(
            slideData(
                "",
                "",
                ""
            )
        )
       adapter = ImageSliderAdapter(list)
        binding.viewPager2.adapter = adapter
        dots = ArrayList()
        indicatorSlide()
        binding.viewPager2.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback(){
            override fun onPageSelected(position: Int) {
                selectedSlide(position)
                super.onPageSelected(position)
            }


        })
    }
    private fun selectedSlide(position: Int) {
        for (i in 0 until list.size){

        }
    }
    private fun indicatorSlide() {
        for (i in 0 until list.size){
        dots.add(TextView(activity))
         dots[i].text = Html.fromHtml("&39679", Html.FROM_HTML_MODE_LEGACY).toString()
        dots[i].textSize = 18f
            binding.dotsIndicator.addView(dots[i])
        }
    }

}