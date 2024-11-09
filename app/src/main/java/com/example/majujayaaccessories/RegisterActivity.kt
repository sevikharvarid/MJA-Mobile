package com.example.majujayaaccessories

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.majujayaaccessories.api.ApiClient
import com.example.majujayaaccessories.api.RegisterRequest
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class RegisterActivity : AppCompatActivity() {

    private lateinit var fullNameEditText: EditText
    private lateinit var phoneEditText: EditText
    private lateinit var emailEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var registerButton: Button
    private lateinit var loginTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        // Mengubah warna status bar agar sesuai dengan background
        window.statusBarColor = ContextCompat.getColor(this, R.color.primary_green)

        fullNameEditText = findViewById(R.id.fullNameEditText)
        phoneEditText = findViewById(R.id.phoneNumberEditText)
        emailEditText = findViewById(R.id.emailEditText)
        passwordEditText = findViewById(R.id.passwordEditText)
        registerButton = findViewById(R.id.registerButton)
        loginTextView = findViewById(R.id.loginTextView)


        loginTextView.setOnClickListener {
            val intent = Intent(this@RegisterActivity, LoginActivity::class.java)
            startActivity(intent)
        }

        registerButton.setOnClickListener {
            val fullName = fullNameEditText.text.toString()
            val phone = phoneEditText.text.toString()
            val email = emailEditText.text.toString()
            val password = passwordEditText.text.toString()

            if (fullName.isEmpty() || phone.isEmpty() || email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Harap isi semua kolom", Toast.LENGTH_SHORT).show()
            } else {
                // Panggil registerUser setelah semua kolom terisi
                registerUser(fullName, phone, email, password)
            }
        }
    }

    private fun registerUser(
        fullName: String,
        phone: String,
        email: String,
        password: String
    ) {
        val request = RegisterRequest(
            full_name = fullName,
            phone = phone,
            email = email,
            password = password
        )

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = ApiClient.instance.registerUser(request)
                withContext(Dispatchers.Main) {
                    if (response.isSuccessful && response.body() != null) {
                        val responseBody = response.body()!!

                        Toast.makeText(
                            this@RegisterActivity,
                            "Pendaftaran berhasil: ${responseBody.message}",
                            Toast.LENGTH_SHORT
                        ).show()

                        // Jika perlu, Anda bisa mengarahkan pengguna ke halaman berikutnya
                        val intent = Intent(this@RegisterActivity, LoginActivity::class.java)
                        startActivity(intent)

                    } else {
                        Toast.makeText(
                            this@RegisterActivity,
                            "Pendaftaran gagal, coba lagi.",
                            Toast.LENGTH_SHORT
                        ).show()
                        Log.e("RegisterActivity", "Response: $response")
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(
                        this@RegisterActivity,
                        "Terjadi kesalahan, coba lagi.",
                        Toast.LENGTH_SHORT
                    ).show()
                    Log.e("RegisterActivity", "Error: ${e.message}")
                }
            }
        }
    }
}
