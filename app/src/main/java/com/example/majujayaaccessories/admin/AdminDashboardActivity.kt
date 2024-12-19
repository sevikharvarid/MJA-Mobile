package com.example.majujayaaccessories.admin

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.majujayaaccessories.R
import com.example.majujayaaccessories.databinding.ActivityAdminDashboardBinding

class AdminDashboardActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAdminDashboardBinding
    private var token: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Inisialisasi view binding untuk activity ini
        binding = ActivityAdminDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Mengatur warna status bar
        window.statusBarColor = ContextCompat.getColor(this, R.color.primary_green)

        // Mengambil token dari SharedPreferences
        val sharedPreferences = getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
        token = sharedPreferences.getString("token", null)

        if (token == null) {
            Log.e("AdminDashboardActivity", "Token tidak ditemukan, pastikan Anda sudah login.")
        } else {
            Log.d("AdminDashboardActivity", "Token ditemukan: $token")
        }

        // Set fragment default (Home) dengan token
        val homeFragment = AdminHomeFragment().apply {
            arguments = Bundle().apply {
                putString("token", token)
            }
        }
        loadFragment(homeFragment)

        binding.bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.menu_home -> {
                    val homeFragment = AdminHomeFragment().apply {
                        arguments = Bundle().apply {
                            putString("token", token)
                        }
                    }
                    loadFragment(homeFragment)
                    true
                }
                R.id.menu_orders -> {
                    val orderFragment = AdminOrderFragment().apply {
                        arguments = Bundle().apply {
                            putString("token", token)
                        }
                    }
                    loadFragment(orderFragment)
                    true
                }
                R.id.menu_history -> {
                    val historyFragment = AdminHistoryFragment().apply {
                        arguments = Bundle().apply {
                            putString("token", token)
                        }
                    }
                    loadFragment(historyFragment)
                    true
                }
                else -> false
            }
        }
    }

    private fun loadFragment(fragment: Fragment) {
        val sharedPreferences = getSharedPreferences("AdminPrefs", Context.MODE_PRIVATE)
        val token = sharedPreferences.getString("token", null)

        if (token != null) {
            fragment.arguments = Bundle().apply {
                putString("token", token)
            }
        } else {
            Log.e("AdminDashboardActivity", "Token tidak ditemukan, fragment tidak dimuat.")
            // Redirect ke layar login jika token tidak ditemukan
        }

        if (!isFinishing && !isDestroyed) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, fragment)
                .commit()
        }
    }
}
