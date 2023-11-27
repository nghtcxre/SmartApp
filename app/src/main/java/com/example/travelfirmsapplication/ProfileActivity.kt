package com.example.travelfirmsapplication

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.lifecycleScope
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.gotrue.OtpType
import io.github.jan.supabase.gotrue.authenticatedSupabaseApi
import io.github.jan.supabase.gotrue.gotrue
import io.github.jan.supabase.network.SupabaseApi
import io.github.jan.supabase.postgrest.postgrest
import io.github.jan.supabase.postgrest.query.Columns
import io.github.jan.supabase.realtime.realtime
import kotlinx.coroutines.launch

class ProfileActivity : AppCompatActivity() {

    private val REQUEST_IMAGE_CAPTURE = 1
    private val REQUEST_IMAGE_PICK = 2

    private lateinit var addressEditText: EditText
    private lateinit var usernameEditText: EditText
    private lateinit var emailEditText: EditText

    private val SBclient: SupabaseClient = CreateSupabaseClient().SBclient

    private lateinit var profileImageView: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
        Load_info_from_db()
        profileImageView = findViewById(R.id.profileImage)
        restoreProfileImage()
        profileImageView.setOnClickListener {
            showImagePicker()
        }
    }

    private fun Load_info_from_db() {
        lifecycleScope.launch {
            try {
                addressEditText = findViewById(R.id.addressEditText)
                usernameEditText = findViewById(R.id.usernameEditText)
                emailEditText = findViewById(R.id.emailEditText)
                val user = SBclient.gotrue.retrieveUserForCurrentSession(updateSession = true)
                val userNameRespone = SBclient.postgrest["Users"].select(columns = Columns.list("name")){
                    eq("id", user.id)
                }.decodeSingle<User>()
                usernameEditText.setText(userNameRespone.name)
                val addressResponse = SBclient.postgrest["Users"].select(columns = Columns.list("address")){
                    eq("id", user.id)
                }.decodeSingle<User>()
                addressEditText.setText(addressResponse.address)
                emailEditText.setText(user.email)

            } catch (e: Exception){
                Log.e("Error", e.toString())
            }
        }
    }


    private fun showImagePicker() {
        val galleryIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(galleryIntent, REQUEST_IMAGE_PICK)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                REQUEST_IMAGE_PICK -> {
                    val selectedImageUri: Uri? = data?.data
                    updateProfileImage(selectedImageUri)
                }
            }
        }
    }

    private fun updateProfileImage(imageUri: Uri?) {
        imageUri?.let {
            try {
                val bitmap: Bitmap = MediaStore.Images.Media.getBitmap(contentResolver, it)
                    profileImageView.setImageBitmap(bitmap)
                saveProfileImageUri(imageUri)
            } catch (e:Exception) {
                e.printStackTrace()
            }
        }
    }


    private fun saveProfileImageUri(imageUri: Uri) {
        val preferences = getPreferences(MODE_PRIVATE)
        val editor = preferences.edit()
        editor.putString("profile_image_uri", imageUri.toString())
        editor.apply()
    }


    private fun restoreProfileImage() {
        val preferences = getPreferences(MODE_PRIVATE)
        val imageUriString = preferences.getString("profile_image_uri", null)

        if (!imageUriString.isNullOrBlank()) {
            val imageUri = Uri.parse(imageUriString)
            updateProfileImage(imageUri)
        }
    }

    fun toMain(view: View) {
        finish()
    }

    fun modifyUser(view: View) {
        lifecycleScope.launch {
            try {
                SBclient.gotrue.retrieveUserForCurrentSession(updateSession = true)
                SBclient.gotrue.modifyUser {
                    email = emailEditText.text.toString()
                }
            } catch (e: Exception){
                Log.e("Error", e.toString())
            }
        }

    }
}