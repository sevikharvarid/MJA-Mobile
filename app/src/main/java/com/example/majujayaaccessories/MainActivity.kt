package com.example.majujayaaccessories

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.android.material.bottomsheet.BottomSheetDialog

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Mengubah warna status bar agar sesuai dengan background
        window.statusBarColor = ContextCompat.getColor(this, R.color.primary_green)

        showBottomSheet()
    }

    private fun showBottomSheet() {
        val bottomSheetDialog = BottomSheetDialog(this)
        val bottomSheetView: View = LayoutInflater.from(this).inflate(R.layout.bottom_sheet_layout, null)

        val createAccountButton: Button = bottomSheetView.findViewById(R.id.createAccountButton)
        val loginButton: Button = bottomSheetView.findViewById(R.id.loginButton)

        createAccountButton.setOnClickListener {
            // Tambahkan aksi untuk "Buat Akun" di sini
        }

        loginButton.setOnClickListener {
            // Tambahkan aksi untuk "Sudah Punya Akun" di sini
        }

        bottomSheetDialog.setContentView(bottomSheetView)
        bottomSheetDialog.show()
    }
}
