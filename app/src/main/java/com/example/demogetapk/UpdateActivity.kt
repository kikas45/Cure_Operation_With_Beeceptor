package com.example.demogetapk

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.Toast
import com.example.demogetapk.data.RetrofitInstancePost
import com.example.demogetapk.databinding.ActivityUpdateBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class UpdateActivity : AppCompatActivity() {
    private lateinit var binding: ActivityUpdateBinding
    private val TAG = "UpdateActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUpdateBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupUI()

        binding.btnSendPost.setOnClickListener {
            if (isNetworkAvailable()) {
                updateUser()
            } else {
                showToastMessage("No Internet Connection")
            }
        }
    }

    private fun setupUI() {
        val userId = intent.getStringExtra("userId") ?: ""
        val firstName = intent.getStringExtra("firstName") ?: ""
        val lastName = intent.getStringExtra("lastName") ?: ""

        binding.apply {
            editTextEnterFirstName.setText(firstName)
            editTextEnterLastName.setText(lastName)
            // Initialize other UI elements if needed
        }
    }

    private fun updateUser() {
        val userId = intent.getStringExtra("userId") ?: ""
        val firstName = binding.editTextEnterFirstName.text.toString().trim()
        val lastName = binding.editTextEnterLastName.text.toString().trim()

        if (firstName.isNotEmpty() && lastName.isNotEmpty()) {
            val data = mapOf("firstName" to firstName, "lastName" to lastName)
            sendUpdateRequest(userId, data)
        } else {
            if (firstName.isEmpty()) {
                binding.editTextEnterFirstName.error = "Input First name"
            }
            if (lastName.isEmpty()) {
                binding.editTextEnterLastName.error = "Input Last name"
            }
        }
    }

    private fun sendUpdateRequest(userId: String, data: Map<String, String>) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = RetrofitInstancePost.api.updateUser(userId, data)

                withContext(Dispatchers.Main) {
                    binding.progressBar.visibility = View.VISIBLE
                    binding.viewCover.visibility = View.VISIBLE
                }

                if (response.isSuccessful) {
                    showToastMessage("User updated successfully")
                    navigateBackToMain()
                } else {
                    showToastMessage("Failed to update user")
                }
            } catch (e: Exception) {
                showToastMessage("Fatal Error occurred")
            } finally {
                withContext(Dispatchers.Main) {
                    binding.progressBar.visibility = View.GONE
                    binding.viewCover.visibility = View.GONE
                }
            }
        }
    }

    private fun navigateBackToMain() {
        startActivity(Intent(applicationContext, MainActivity::class.java))
        finish()
    }

    private fun showToastMessage(message: String) {
        runOnUiThread {
            Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT).show()
        }
    }

    private fun isNetworkAvailable(): Boolean {
        val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetworkInfo = connectivityManager.activeNetworkInfo
        return activeNetworkInfo != null && activeNetworkInfo.isConnected
    }
}
