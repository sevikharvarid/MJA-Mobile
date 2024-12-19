package com.example.majujayaaccessories

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.example.majujayaaccessories.api.ApiClient
import com.example.majujayaaccessories.response.UserProfileResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProfileFragment : Fragment() {

    private lateinit var profileImageView: ImageView
    private lateinit var fullNameEditText: EditText
    private lateinit var emailEditText: EditText
    private lateinit var phoneEditText: EditText
    private lateinit var storeNameEditText: EditText
    private lateinit var addressEditText: EditText

    private var token: String? = null
    private var id: Int? = null

    private lateinit var loadingDialog: android.app.AlertDialog


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            token = it.getString("token")
            id = it.getString("userId")?.toInt()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_profile, container, false)

        // Deklarasi dan inisialisasi komponen UI berdasarkan ID
        profileImageView = view.findViewById(R.id.profileImageView)
        val editProfileButton: Button = view.findViewById(R.id.editProfileButton)
        fullNameEditText = view.findViewById(R.id.fullNameEditText)
        emailEditText = view.findViewById(R.id.emailEditText)
        phoneEditText = view.findViewById(R.id.phoneEditText)
        storeNameEditText = view.findViewById(R.id.storeNameEditText)
        addressEditText = view.findViewById(R.id.addressEditText)

        editProfileButton.setOnClickListener {
            Toast.makeText(requireContext(), "Edit Profile clicked", Toast.LENGTH_SHORT).show()

            // Buat instance dari UpdateProfileFragment
            val updateProfileFragment = UpdateProfileFragment()

            // Kirim token dan userId ke UpdateProfileFragment
            val args = Bundle()
            args.putString("token", token)
            args.putString("userId", id.toString())
            updateProfileFragment.arguments = args

            // Lakukan transisi ke UpdateProfileFragment
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, updateProfileFragment) // R.id.fragmentContainer adalah ID container fragment
                .addToBackStack(null) // Menambahkan ke back stack agar bisa kembali ke fragment sebelumnya
                .commit()
        }


        // Panggil API untuk mengambil data profile
        if (token != null) {
            Log.d("ProfileFragment", "Berhasil kok ini id nya : $id")
            getUserProfile(token = token!!, id = id!!) // Panggil fungsi untuk mengambil data profil
        } else {
            Log.d("ProfileFragment", "Tidak berhasil token tidak tersedia")
            Toast.makeText(requireContext(), "Token tidak tersedia. Silakan login kembali.", Toast.LENGTH_SHORT).show()
        }

        return view
    }

    private fun getUserProfile(token: String,id: Int) {
        val userId = id
        val authToken = "Bearer $token"

        // Tampilkan dialog loading
        showLoadingDialog()

        ApiClient.instance.getUserProfile(userId, authToken).enqueue(object : Callback<UserProfileResponse> {
            override fun onResponse(call: Call<UserProfileResponse>, response: Response<UserProfileResponse>) {
                Log.d("ProfileFragment", "User Profile: $response")

                // Sembunyikan dialog loading
                hideLoadingDialog()

                if (response.isSuccessful) {
                    val userProfile = response.body()?.data
                    Log.d("ProfileFragment", "User Profile: $userProfile")
                    userProfile?.let { updateUI(it) }
                } else {
                    Toast.makeText(requireContext(), "Failed to load profile", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<UserProfileResponse>, t: Throwable) {
                // Sembunyikan dialog loading
                hideLoadingDialog()

                Toast.makeText(requireContext(), "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
    private fun updateUI(userProfile: UserProfile) {
        fullNameEditText.setText(userProfile.full_name)
        emailEditText.setText(userProfile.email)
        phoneEditText.setText(userProfile.phone)
        addressEditText.setText(userProfile.store?.store_address)
        storeNameEditText.setText(userProfile.store?.store_name ?: "No store")
        Glide.with(this@ProfileFragment)
            .load(userProfile.image_url)
            .placeholder(R.drawable.ic_home)
            .transform(CircleCrop())
            .into(profileImageView)
    }

    private fun showLoadingDialog() {
        val builder = android.app.AlertDialog.Builder(requireContext())
        builder.setView(R.layout.loading_dialog)
        builder.setCancelable(false) // Membuat dialog tidak bisa ditutup dengan back button
        loadingDialog = builder.create()
        loadingDialog.show()
    }

    private fun hideLoadingDialog() {
        if (::loadingDialog.isInitialized) {
            loadingDialog.dismiss()
        }
    }

}
