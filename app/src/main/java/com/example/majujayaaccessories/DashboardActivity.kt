package com.example.majujayaaccessories

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.majujayaaccessories.ProfileFragment
import com.example.majujayaaccessories.databinding.ActivityDashboardBinding

class DashboardActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDashboardBinding
    private var token: String? = null
    private var id: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Mengubah warna status bar agar sesuai dengan background
        window.statusBarColor = ContextCompat.getColor(this, R.color.primary_green)

        // Ambil token dari SharedPreferences
        val sharedPreferences = getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
        token = sharedPreferences.getString("token", null)
        id = sharedPreferences.getString("userId", null)?.toInt()

        Log.e("DashboardActivity", "UserID : $id")


        if (token == null) {
            Log.e("DashboardActivity", "Token tidak ditemukan, pastikan Anda sudah login.")
        } else {
            Log.d("DashboardActivity", "Token ditemukan: $token")
        }

        // Set default fragment dengan token
        val homeFragment = HomeFragment().apply {
            arguments = Bundle().apply {
                putString("token", token)
                putString("userId", id.toString())
            }
        }

        loadFragment(homeFragment)

        binding.bottomNavigation.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.menu_home -> {
                    val homeFragment = HomeFragment().apply {
                        arguments = Bundle().apply {
                            putString("token", token)
                            putString("userId", id.toString())
                        }
                    }
                    loadFragment(homeFragment)
                    true
                }
                R.id.menu_orders -> {
                    val orderFragment = OrderFragment().apply {
                        arguments = Bundle().apply {
                            putString("token", token)
                            putString("userId", id.toString())
                        }
                    }
                    loadFragment(orderFragment) // Ganti dengan fragment OrdersFragment jika perlu
                    true
                }
                R.id.menu_history -> {
                    val historyFragment = HistoryFragment().apply {
                        arguments = Bundle().apply {
                            putString("token", token)
                            putString("userId", id.toString())
                        }
                    }
                    loadFragment(historyFragment) // Ganti dengan fragment OrdersFragment jika perlu

                    true
                }
                R.id.menu_profile -> {
                    val profileFragment = ProfileFragment().apply {
                        arguments = Bundle().apply {
                            putString("token", token)
                            putString("userId", id?.toString())
                        }
                    }
                    loadFragment(profileFragment) // Ganti dengan fragment ProfileFragment jika perlu
                    true
                }
                else -> false
            }
        }
    }

    private fun loadFragment(fragment: Fragment) {
        val sharedPreferences = getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
        val token = sharedPreferences.getString("token", null)
        val id = sharedPreferences.getString("userId", null)

        if (token != null) {
            fragment.arguments = Bundle().apply {
                putString("token", token)
                putString("userId", id)
            }
        } else {
            Log.e("DashboardActivity", "Token tidak ditemukan, fragment tidak dimuat.")
            // Redirect ke login screen jika token tidak ditemukan
        }

        if (!isFinishing && !isDestroyed) { // Pastikan activity belum dihentikan
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, fragment)
                .commit()
        }
    }
}
