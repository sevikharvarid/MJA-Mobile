package com.example.majujayaaccessories

import android.content.Context
import android.content.Intent
import android.widget.Toast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.majujayaaccessories.databinding.ActivityLoginBinding
import com.example.majujayaaccessories.request.LoginRequest
import com.example.majujayaaccessories.response.LoginResponse
import com.example.majujayaaccessories.api.ApiClient
import com.google.android.material.bottomsheet.BottomSheetDialog

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Mengubah warna status bar agar sesuai dengan background
        window.statusBarColor = ContextCompat.getColor(this, R.color.primary_green)

        // Event untuk pindah ke halaman Daftar jika belum punya akun
        binding.registerTextView.setOnClickListener {
            // Di sini, arahkan ke activity pendaftaran jika diperlukan
            val intent = Intent(this@LoginActivity, RegisterActivity::class.java)
            startActivity(intent)
        }

        // Panggil fungsi login saat tombol "Masuk" diklik
        binding.loginButton.setOnClickListener {
            val email = binding.emailEditText.text.toString()
            val password = binding.passwordEditText.text.toString()

            val loginRequest = LoginRequest(email, password)

            ApiClient.instance.login(loginRequest).enqueue(object : Callback<LoginResponse> {
                override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                    if (response.isSuccessful && response.body() != null) {
                        val loginResponse = response.body()
                        // Arahkan ke halaman Dashboard jika login berhasil
                        if(loginResponse?.data?.store != null){
                            // Simpan token di SharedPreferences
                            val sharedPreferences = getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
                            val editor = sharedPreferences.edit()
                            val token = loginResponse.data.token
                            editor.putString("token", token)
                            editor.apply()

                            Log.d("LoginActivity", "Token: $token")

                            val intent = Intent(this@LoginActivity, DashboardActivity::class.java)
                            startActivity(intent)
                            finish()
                        }else{
                            val intent = Intent(this@LoginActivity, RegisterStoreActivity::class.java)
                            intent.putExtra("token", loginResponse?.data?.token)
                            intent.putExtra("userId", loginResponse?.data?.id.toString())
                            startActivity(intent)
                        }

                        // Arahkan ke halaman berikutnya jika login berhasil
                    } else {
                        showErrorBottomSheet("Terjadi kesalahan login, silahkan mencoba kembali")
                        // Mengosongkan kolom email dan password
                        binding.emailEditText.text.clear()
                        binding.passwordEditText.text.clear()
                    }
                }

                override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                    showErrorBottomSheet("Gagal terhubung ke server: ${t.message}")
                    // Mengosongkan kolom email dan password
                    binding.emailEditText.text.clear()
                    binding.passwordEditText.text.clear()
                }
            })
        }
    }

    private fun showErrorBottomSheet(message: String) {
        val bottomSheetDialog = BottomSheetDialog(this)
        val view: View = LayoutInflater.from(this).inflate(R.layout.layout_error_bottom_sheet, null)
        val messageTextView: TextView = view.findViewById(R.id.errorMessageTextView)
        messageTextView.text = message
        bottomSheetDialog.setContentView(view)
        bottomSheetDialog.show()
    }
}
