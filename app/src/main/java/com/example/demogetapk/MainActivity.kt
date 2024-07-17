package com.example.demogetapk

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.demogetapk.adapter.UserAdapter
import com.example.demogetapk.data.UserViewModel
import com.example.demogetapk.databinding.ActivityMainBinding
import com.example.demogetapk.fecth.UserModel

class MainActivity : AppCompatActivity(), UserAdapter.OnItemClickListener {
    private lateinit var binding: ActivityMainBinding
    private val TAG = "MainActivity"

    private val mUserViewModel: UserViewModel by viewModels()
    private val adapter by lazy { UserAdapter(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupRecyclerView()

        binding.floatingActionButton.setOnClickListener {
            startActivity(Intent(applicationContext, AddActivity::class.java))
        }

        observeViewModel()
        mUserViewModel.getUsers()
    }

    private fun setupRecyclerView() {
        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
    }

    private fun observeViewModel() {
        binding.progressBar.visibility = View.VISIBLE
        mUserViewModel.userList.observe(this, Observer { users ->
            users?.let {
                adapter.setData(it)
                if (it.isEmpty()){
                    binding.progressBar.visibility = View.GONE
                    binding.textErrorText.visibility = View.VISIBLE
                }else{
                    binding.progressBar.visibility = View.GONE
                }
            }
        })
    }

    override fun onItemClicked(userModel: UserModel) {
        showDeleteConfirmation(userModel)

    }

    private fun showToastMessage(messages: String) {

        runOnUiThread {
            Toast.makeText(applicationContext, messages, Toast.LENGTH_SHORT).show()
        }

    }






    private fun showDeleteConfirmation(userModel: UserModel) {
        val context = this
        AlertDialog.Builder(context)
            .setTitle("Manage User")
            .setMessage("What would you like to do with ${userModel.firstName} ${userModel.lastName}?")
            .setPositiveButton("Update") { dialog, _ ->

                navigateToUpdateActivity(userModel)
                dialog.dismiss()
            }
            .setNegativeButton("Delete") { dialog, _ ->
                mUserViewModel.delete(userModel)
                showToastMessage("User deleted")
                dialog.dismiss()
            }
            .setNeutralButton("Cancel") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }

    private fun navigateToUpdateActivity(userModel: UserModel) {
        val intent = Intent(this, UpdateActivity::class.java).apply {
            putExtra("userId", userModel.id) // Assuming you have an id field in your UserModel
            putExtra("firstName", userModel.firstName)
            putExtra("lastName", userModel.lastName)
        }
        startActivity(intent)
    }


}
