package com.sro.stupa.viewModel

import MainRepository
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sro.stupa.Model.User

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class UserViewModel(private val userRepository: MainRepository) : ViewModel() {


    fun insertUser(user: User) {
        viewModelScope.launch(Dispatchers.IO) {
            userRepository.insertUser(user)
        }
    }

     fun getUserByEmailAndPassword(email: String, password: String): User? {
        return userRepository.getUserByEmailAndPassword(email, password)
    }


    fun getAllUsers(): LiveData<List<User>> {
        return userRepository.getAllUsers()
    }
}
