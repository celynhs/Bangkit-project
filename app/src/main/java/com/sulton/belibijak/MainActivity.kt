package com.sulton.belibijak

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavOptions
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.sulton.belibijak.databinding.ActivityMainBinding
import com.sulton.belibijak.ui.cart.CartFragment

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        val navView: BottomNavigationView = binding.navView

        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.

        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home, R.id.navigation_cart, R.id.navigation_deliver, R.id.navigation_profile
            )
        )

        if (intent.hasExtra("cart")) {
            val item = navView.menu.findItem(R.id.navigation_cart)
            val paket = intent.getStringExtra("Title")
            val price = intent.getStringExtra("Price")
            val pcs = intent.getStringExtra("Pcs")
            Log.d("testData", paket!!)
            val cartFragment = CartFragment.newInstance(paket!!, price!!, pcs!!)

            NavigationUI.onNavDestinationSelected(item, navController)
        }

        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }

}