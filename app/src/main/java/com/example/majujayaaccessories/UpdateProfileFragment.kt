package com.example.majujayaaccessories

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.example.majujayaaccessories.api.ApiClient
import com.example.majujayaaccessories.response.UserUpdateRequest
import com.example.majujayaaccessories.response.UserUpdateResponse
import com.google.firebase.storage.FirebaseStorage
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.UUID

class UpdateProfileFragment : Fragment() {

    private lateinit var fullNameEditText: EditText
    private lateinit var emailEditText: EditText
    private lateinit var phoneEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var profileImageView: ImageView
    private var imageUri: Uri? = null
    private val storageReference = FirebaseStorage.getInstance().reference

    private var userId: Int? = null
    private var token: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            token = it.getString("token")
            userId = it.getString("userId")?.toInt()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_update_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Inisialisasi EditText dari layout
        fullNameEditText = view.findViewById(R.id.fullNameEditText)
        emailEditText = view.findViewById(R.id.emailEditText)
        phoneEditText = view.findViewById(R.id.phoneEditText)
        passwordEditText = view.findViewById(R.id.passwordEditText)
        profileImageView = view.findViewById(R.id.profileImageViewUpdate)


        if (userId == -1 || token == null) {
            Toast.makeText(requireContext(), "Gagal mengambil data pengguna", Toast.LENGTH_SHORT).show()
            return
        }

        val saveButton = view.findViewById<View>(R.id.saveButton)
        saveButton.setOnClickListener {
            Log.d("UpdateFragment","masuk ke mana nih?")
            if (validateInput()) {
                Log.d("UpdateFragment","ke validate input kah?")

                updateProfile()
            }
        }

        val changePictureButton  = view.findViewById<View>(R.id.changePictureText)

        // Buka galeri ketika tombol "Change Picture" ditekan
        changePictureButton.setOnClickListener {
            openGallery()
        }
    }

    private fun validateInput(): Boolean {
        val fullName = fullNameEditText.text.toString().trim()
        val email = emailEditText.text.toString().trim()
        val phone = phoneEditText.text.toString().trim()
        val password = passwordEditText.text.toString().trim()

        return when {
            fullName.isEmpty() -> {
                Toast.makeText(requireContext(), "Nama lengkap harus diisi", Toast.LENGTH_SHORT).show()
                false
            }
            email.isEmpty() -> {
                Toast.makeText(requireContext(), "Email harus diisi", Toast.LENGTH_SHORT).show()
                false
            }
            phone.isEmpty() -> {
                Toast.makeText(requireContext(), "Nomor telepon harus diisi", Toast.LENGTH_SHORT).show()
                false
            }
            password.isEmpty() -> {
                Toast.makeText(requireContext(), "Password harus diisi", Toast.LENGTH_SHORT).show()
                false
            }
            imageUri == null -> {
                Toast.makeText(requireContext(), "Foto harus di-upload", Toast.LENGTH_SHORT).show()
                false
            }
            else -> true
        }
    }

    private fun updateProfile() {
        val request = UserUpdateRequest(
            email = emailEditText.text.toString(),
            full_name = fullNameEditText.text.toString(),
            phone = phoneEditText.text.toString(),
            password = passwordEditText.text.toString(),
            image_url = "" // Default empty
        )

        // Jika ada gambar, unggah dan dapatkan URL-nya
        imageUri?.let { uri ->
            uploadImageToFirebase(uri) { imageUrl ->
                // Set imageUrl jika upload gambar berhasil
                request.image_url = imageUrl
                sendUpdateRequest(request)
            }
        } ?: sendUpdateRequest(request)  // Jika tidak ada gambar, langsung kirim request
    }

    private fun sendUpdateRequest(request: UserUpdateRequest) {
        ApiClient.instance.updateUser(userId!!, request, "Bearer $token")
            .enqueue(object : Callback<UserUpdateResponse> {
                override fun onResponse(
                    call: Call<UserUpdateResponse>,
                    response: Response<UserUpdateResponse>
                ) {
                    if (response.isSuccessful) {
                        Toast.makeText(requireContext(), "Data berhasil diperbarui", Toast.LENGTH_SHORT).show()
                        parentFragmentManager.popBackStack()
                    } else {
                        Toast.makeText(requireContext(), "Gagal memperbarui data", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<UserUpdateResponse>, t: Throwable) {
                    Toast.makeText(requireContext(), "Error: ${t.message}", Toast.LENGTH_SHORT).show()
                }
            })
    }

    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        galleryLauncher.launch(intent)
    }

    private val galleryLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            imageUri = result.data?.data
            profileImageView.setImageURI(imageUri) // Tampilkan gambar di ImageView

            // Memuat gambar dengan transformasi lingkaran
            Glide.with(requireContext())
                .load(imageUri)
                .transform(CircleCrop()) // Mengubah gambar menjadi lingkaran
                .into(profileImageView)

            // Aktifkan tombol save setelah foto dipilih
            view?.findViewById<Button>(R.id.saveButton)?.isEnabled = true
        }
    }


    private fun uploadImageToFirebase(imageUri: Uri, onSuccess: (String) -> Unit) {
        val fileName = "profile_images/${UUID.randomUUID()}.jpg"
        val ref = storageReference.child(fileName)

        ref.putFile(imageUri)
            .addOnSuccessListener {
                ref.downloadUrl.addOnSuccessListener { uri ->
                    val downloadUrl = uri.toString()
                    onSuccess(downloadUrl) // Panggil callback dengan URL gambar
                    Toast.makeText(requireContext(), "Image uploaded successfully. URL: $downloadUrl", Toast.LENGTH_LONG).show()
                }
            }
            .addOnFailureListener {
                Toast.makeText(requireContext(), "Failed to upload image", Toast.LENGTH_SHORT).show()
            }
    }
}
