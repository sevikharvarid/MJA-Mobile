package com.example.majujayaaccessories

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.majujayaaccessories.R

class ProfileFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_profile, container, false)

        // Deklarasi dan inisialisasi komponen UI berdasarkan ID
        val profileImageView: ImageView = view.findViewById(R.id.profileImageView)
        val editProfileButton: Button = view.findViewById(R.id.editProfileButton)
        val fullNameEditText: EditText = view.findViewById(R.id.fullNameEditText)
        val emailEditText: EditText = view.findViewById(R.id.emailEditText)
        val phoneEditText: EditText = view.findViewById(R.id.phoneEditText)
        val storeNameEditText: EditText = view.findViewById(R.id.storeNameEditText)
        val addressEditText: EditText = view.findViewById(R.id.addressEditText)

        // Set up event listener untuk tombol Edit Profil
        editProfileButton.setOnClickListener {
            // Handle tombol edit profil klik
            Toast.makeText(requireContext(), "Edit Profile clicked", Toast.LENGTH_SHORT).show()
        }

        // Kembali ke tampilan yang sudah di-inflate
        return view
    }
}
