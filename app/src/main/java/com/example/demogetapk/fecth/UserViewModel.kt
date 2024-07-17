package com.example.demogetapk.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.demogetapk.fecth.UserModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class UserViewModel : ViewModel() {

    private val userRepository = UserRepository()

    private val _userList = MutableLiveData<List<UserModel>>()
    val userList: LiveData<List<UserModel>> = _userList

    fun getUsers() {
        viewModelScope.launch(Dispatchers.IO) {
            val response = userRepository.getUsers()
            if (response.isSuccessful) {
                _userList.postValue(response.body())
            }
        }
    }

    fun delete(userModel: UserModel) {
        viewModelScope.launch(Dispatchers.IO) {
            val response = userRepository.deleteUser(userModel.id)
            if (response.isSuccessful) {
                getUsers() // Refresh the list after deletion
            }
        }
    }
}
