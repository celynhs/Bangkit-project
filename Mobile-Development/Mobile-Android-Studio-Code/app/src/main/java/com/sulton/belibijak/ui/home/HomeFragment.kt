package com.sulton.belibijak.ui.home


import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.text.Html
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.sulton.belibijak.R
import com.sulton.belibijak.data.local.TestPromo
import com.sulton.belibijak.data.local.UserPreference
import com.sulton.belibijak.databinding.FragmentHomeBinding
import com.sulton.belibijak.ui.auth.AuthActivity
import com.sulton.belibijak.ui.auth.LoginViewModel
import com.sulton.belibijak.ui.auth.ViewModelFactory
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch


class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var logViewModel: LoginViewModel
    private lateinit var adapter: ImageSliderAdapter
    private lateinit var rAdapter: ListRecommendedAdapter
    private val list = TestPromo.carouselHome
    private lateinit var dots: ArrayList<TextView>
    private val recViewModel: RecommendViewModel by viewModels {
        StoryModelFactory(requireContext())
    }

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
        setName(pref)

        binding.apply {
            rvRecommended.setHasFixedSize(true)
            rvRecommended.layoutManager = GridLayoutManager(requireActivity(), 2)
        }
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
            val alertDialog = AlertDialog.Builder(requireContext())
            .setTitle("Logout")
            .setMessage("Are you sure you want to logout?")
            .setPositiveButton("Yes") { dialog: DialogInterface, which: Int ->
                // Perform logout action
                logViewModel.logOut()
                val intent = Intent(requireContext(), AuthActivity::class.java)
                startActivity(intent)
                requireActivity().finish()

            }
            .setNegativeButton("No") { dialog: DialogInterface, which: Int ->
                // Do nothing, dismiss the dialog
                dialog.dismiss()
            }
            .create()

            alertDialog.show()

        }
        with(binding){
            Glide.with(this@HomeFragment)
                .load(R.drawable.gojokun)
                .circleCrop()
                .into(ivUser)
        }

        getRecommend(pref)
    }

    override fun onDestroy() {
        super.onDestroy()
    }
    fun setName(pref : UserPreference){
        viewLifecycleOwner.lifecycleScope.launch{
            val name = "Hi, ${pref.getName().first()}"
          binding.tvWelcomeHome.text = name
        }
    }
    private fun selectedSlide(position: Int) {
    for (i in list.indices){
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


    private fun getRecommend(pref: UserPreference){
        var bud : Double? = 1000.0
        viewLifecycleOwner.lifecycleScope.launch{
            bud = pref.getBudget().first()
        }
        recViewModel.getRecommendItem(bud?: 0.0)
        recViewModel.loading.observe(viewLifecycleOwner){
            showLoading(it)
        }
        recViewModel.recommendItem.observe(viewLifecycleOwner) {
            rAdapter = ListRecommendedAdapter(it)
            binding.rvRecommended.adapter = rAdapter
        }

    }
    private fun showLoading(isLoading: Boolean) {
        binding.progressBar3.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}