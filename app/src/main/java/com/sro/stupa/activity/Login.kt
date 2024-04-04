package com.sro.stupa.activity

import MainRepository
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.room.Room
import com.sro.stupa.Model.AppDatabase
import com.sro.stupa.Model.UserDao
import com.sro.stupa.databinding.ActivityLoginBinding
import com.sro.stupa.viewModel.UserViewModel
import com.sro.stupa.viewModel.UserViewModelFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * LoginActivity handles user login functionality.
 */
class Login : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var viewModel: UserViewModel
    private lateinit var userDao: UserDao
    private lateinit var mainRepository: MainRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize UI components
        initUI()
    }

    /**
     * Initializes the UI components and sets up the click listeners.
     */
    private fun initUI() {
        // Initialize UserDao and MainRepository
        userDao = Room.databaseBuilder(applicationContext, AppDatabase::class.java, "my_database").build().userDao()
        mainRepository = MainRepository(userDao)

        // Initialize ViewModel
        viewModel = ViewModelProvider(this, UserViewModelFactory(mainRepository)).get(UserViewModel::class.java)

        // Set click listeners
        binding.loginButton.setOnClickListener {
            if (validation()) {
                loginUser()
            }
        }
        binding.clickToRegister.setOnClickListener {
            startActivity(Intent(this@Login, Register::class.java))
        }
    }

    /**
     * Validates user input fields.
     * @return true if the input is valid, false otherwise.
     */
    private fun validation(): Boolean {
        if (binding.emailEditText.text?.isEmpty()!!) {
            binding.emailEditText.error = "This cannot be empty."
            binding.emailEditText.requestFocus()
            return false
        }

        if (binding.passwordEditText.text?.isEmpty()!!) {
            binding.passwordEditText.error = "This cannot be empty."
            binding.passwordEditText.requestFocus()
            return false
        }

        return true
    }

    /**
     * Attempts to log in the user.
     */
    private fun loginUser() {
        try {
            CoroutineScope(Dispatchers.IO).launch {
                val user = viewModel.getUserByEmailAndPassword(
                    binding.emailEditText.text.toString(),
                    binding.passwordEditText.text.toString()
                )

                withContext(Dispatchers.Main) {
                    Toast.makeText(this@Login, "Logged in Successful", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this@Login, Home::class.java)
                    intent.putExtra("name", user?.name)
                    startActivity(intent)
                    finishAffinity()
                }
            }
        } catch (e: Exception) {
            Toast.makeText(this@Login, e.localizedMessage!!.toString(), Toast.LENGTH_SHORT).show()
        }
    }
}
