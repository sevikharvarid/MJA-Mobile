package com.example.majujayaaccessories

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.majujayaaccessories.api.ApiClient
import com.example.majujayaaccessories.api.RegisterRequest
import com.example.majujayaaccessories.request.StoreRequest
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class RegisterStoreActivity : AppCompatActivity() {

    private lateinit var storeNameEditText: EditText
    private lateinit var addressEditText: EditText
    private lateinit var registerButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register_store)

        // Mengubah warna status bar agar sesuai dengan background
        window.statusBarColor = ContextCompat.getColor(this, R.color.primary_green)

        // Ambil data dari intent
        val token = intent.getStringExtra("token")
        val userId = intent.getStringExtra("userId")

        storeNameEditText = findViewById(R.id.storeNameEditText)
        addressEditText = findViewById(R.id.addressEditText)
        registerButton = findViewById(R.id.registerFinalButton)

        registerButton.setOnClickListener {
            val storeName = storeNameEditText.text.toString()
            val address = addressEditText.text.toString()

            if (storeName.isEmpty() || address.isEmpty()) {
                Toast.makeText(this, "Harap isi semua kolom", Toast.LENGTH_SHORT).show()
            } else {
                if (userId != null) {
                    registerStore(token!!, userId.toInt(),storeName!!, address!!)
                }
            }
        }
    }

    private fun registerStore(token: String, userId: Int, storeName: String, storeAddress: String) {
        // Buat permintaan untuk mendaftarkan toko
        val storeRequest =
            StoreRequest(user_id = userId, store_name = storeName, store_address = storeAddress)

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val tokenRequest = "Bearer $token"
                val storeResponse = ApiClient.instance.registerStore(tokenRequest, storeRequest)

                withContext(Dispatchers.Main) {
                    if (storeResponse.isSuccessful && storeResponse.body() != null) {
                        val responseBody = storeResponse.body()!!
                        Toast.makeText(
                            this@RegisterStoreActivity,
                            "Pendaftaran toko berhasil Pesan: ${responseBody.message}",
                            Toast.LENGTH_SHORT
                        ).show()
                        val intent = Intent(this@RegisterStoreActivity, DashboardActivity::class.java)
                        startActivity(intent)
                        finish()
                    } else {
                        Toast.makeText(
                            this@RegisterStoreActivity,
                            "Pendaftaran toko gagal: ${storeResponse.errorBody()?.string()}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(
                        this@RegisterStoreActivity,
                        "Terjadi kesalahan saat mendaftar toko, coba lagi.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }


}
