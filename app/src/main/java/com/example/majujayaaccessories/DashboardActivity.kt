package com.example.majujayaaccessories

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.majujayaaccessories.databinding.ActivityDashboardBinding

class DashboardActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDashboardBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Mengubah warna status bar agar sesuai dengan background
        window.statusBarColor = ContextCompat.getColor(this, R.color.primary_green)

        // Set default fragment
        loadFragment(HomeFragment())

        binding.bottomNavigation.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.menu_home -> {
                    loadFragment(HomeFragment())
                    true
                }
                R.id.menu_orders -> {
//                    loadFragment(OrdersFragment())
                    loadFragment(HomeFragment())

                    true
                }
                R.id.menu_history -> {
//                    loadFragment(HistoryFragment())
                    loadFragment(HomeFragment())

                    true
                }
                R.id.menu_profile -> {
//                    loadFragment(ProfileFragment())
                    loadFragment(HomeFragment())

                    true
                }
                else -> false
            }
        }
    }

    private fun loadFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, fragment)
            .commit()
    }
}
