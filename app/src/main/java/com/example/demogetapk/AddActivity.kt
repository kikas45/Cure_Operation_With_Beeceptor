package com.example.demogetapk

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.demogetapk.data.RetrofitInstancePost
import com.example.demogetapk.databinding.ActivityAddBinding
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AddActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddBinding
    val TAG = "MainActivity"

    private val handler: Handler by lazy {
        Handler(Looper.getMainLooper())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.apply {
            btnSendPost.setOnClickListener {
                progressBar.visibility = View.VISIBLE
                viewCover.visibility = View.VISIBLE
                hideKeyBoard(editTextEnterFirstName)

                handler.postDelayed(Runnable {
                    if (isNetworkAvailable()) {
                        createUserForm()
                    } else {
                        showToastMessage("No Internet Connection")
                        hideKeyBoard(editTextEnterFirstName)
                        progressBar.visibility = View.GONE
                        viewCover.visibility = View.GONE
                    }
                }, 3000)
            }
        }

    }

    private fun createUserForm() {
        binding.apply {
            val get_first_name = editTextEnterFirstName.text.toString().trim()
            val get_last_name = editTextEnterLastName.text.toString().trim()

            if (get_first_name.isNotEmpty() && get_last_name.isNotEmpty()) {

                val dataMap = HashMap<String, String>()
                dataMap.put("firstName", get_first_name)
                dataMap.put("lastName", get_last_name)

                sendRequest(dataMap)

            } else {
                if (get_first_name.isEmpty()) {
                    editTextEnterFirstName.error = "Input First name "
                }
                if (get_last_name.isEmpty()) {
                    editTextEnterLastName.error = "Input First name "
                }

            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun sendRequest(data: Map<String, String>) = CoroutineScope(Dispatchers.IO).launch {
        try {
            val response = RetrofitInstancePost.api.postData(data)

            withContext(Dispatchers.Main) {
                binding.progressBar.visibility = View.VISIBLE
                binding.viewCover.visibility = View.VISIBLE
            }

            if (response.isSuccessful) {
                Log.d(TAG, "Response: ${Gson().toJson(response)}")

                withContext(Dispatchers.Main) {
                    showToastMessage("Response Successful")
                    startActivity(Intent(applicationContext, MainActivity::class.java))
                    finish()
                    binding.progressBar.visibility = View.GONE
                    binding.viewCover.visibility = View.GONE
                }

            } else {
                Log.e(TAG, response.errorBody().toString())
                withContext(Dispatchers.Main) {
                    showToastMessage("Response Failed")

                    binding.progressBar.visibility = View.GONE
                    binding.viewCover.visibility = View.GONE
                }
            }
        } catch (e: Exception) {
            Log.e(TAG, e.toString())
            withContext(Dispatchers.Main) {
                showToastMessage("Fatal Error")

                binding.progressBar.visibility = View.GONE
                binding.viewCover.visibility = View.GONE
            }
        }
    }

    private fun hideKeyBoard(editText: EditText) {
        try {
            editText.clearFocus()
            val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(editText.windowToken, 0)
        } catch (ignored: java.lang.Exception) {
        }
    }



    private fun isNetworkAvailable(): Boolean {
        val connectivityManager =
            getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetworkInfo = connectivityManager.activeNetworkInfo
        return activeNetworkInfo != null && activeNetworkInfo.isConnected
    }

    private fun showToastMessage(messages: String) {

        runOnUiThread {
            Toast.makeText(applicationContext, messages, Toast.LENGTH_SHORT).show()
        }

    }
}