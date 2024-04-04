package com.sro.stupa.activity

import MainRepository
import android.R
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.room.Room
import com.sro.stupa.Constant
import com.sro.stupa.Model.AppDatabase
import com.sro.stupa.Model.User
import com.sro.stupa.Model.UserDao
import com.sro.stupa.databinding.ActivityRegisterBinding
import com.sro.stupa.viewModel.UserViewModel
import com.sro.stupa.viewModel.UserViewModelFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * RegisterActivity handles user registration functionality.
 */
class Register : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding
    private lateinit var viewModel: UserViewModel
    private lateinit var userDao: UserDao
    private lateinit var mainRepository: MainRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize UI components
        initUI()
    }

    /**
     * Initializes the UI components and sets up necessary listeners.
     */
    private fun initUI() {
        // Initialize UserDao and MainRepository
        userDao = Room.databaseBuilder(applicationContext, AppDatabase::class.java, "my_database").build().userDao()
        mainRepository = MainRepository(userDao)

        // Initialize ViewModel
        viewModel = ViewModelProvider(this, UserViewModelFactory(mainRepository)).get(UserViewModel::class.java)

        // Set up the ArrayAdapter for the country dropdown
        val countries = Constant.countryList
        val adapter = ArrayAdapter(this, R.layout.simple_dropdown_item_1line, countries)
        binding.countryAutoCompleteTextView.setAdapter(adapter)

        // Set listener to handle item selection in the country dropdown
        binding.countryAutoCompleteTextView.setOnItemClickListener { _, _, position, _ ->
            val selectedCountry = adapter.getItem(position).toString()
            binding.countryAutoCompleteTextView.setText(selectedCountry)
        }

        // Set click listener for the register button
        binding.registerButton.setOnClickListener {
            if (validation()) {
                registerUser()
            }
        }

        // Set click listener for the "Click to Login" button
        binding.clickToRegister.setOnClickListener {
            startActivity(Intent(this@Register, Login::class.java))
        }
    }

    /**
     * Validates user input fields.
     * @return true if the input is valid, false otherwise.
     */
    private fun validation(): Boolean {
        if (binding.nameEditText.text.toString().length > 15) {
            binding.nameEditText.error = "Name must not exceed 15 characters"
            binding.nameEditText.requestFocus()
            return false
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(binding.emailEditText.text.toString()).matches()) {
            binding.emailEditText.error = "Email is not valid"
            binding.emailEditText.requestFocus()
            return false
        }

        val pattern = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@\$!%*?&])[A-Za-z\\d@\$!%*?&]{8,}$".toRegex()
        if (!pattern.matches(binding.passwordEditText.text.toString())) {
            Toast.makeText(
                this@Register,
                "Password must contain at least one lowercase letter, one uppercase letter, one digit, one special character, and be at least 8 characters long.",
                Toast.LENGTH_SHORT
            ).show()
            binding.passwordEditText.requestFocus()
            return false
        }

        if (binding.phoneNumberEditText.text.toString().length != 10) {
            binding.phoneNumberEditText.error = "Phone number must be of 10 digits only"
            binding.phoneNumberEditText.requestFocus()
            return false
        }

        return true
    }

    /**
     * Registers the user.
     */
    private fun registerUser() {
        val user = User(
            name = binding.nameEditText.text.toString(),
            phoneNumber = binding.phoneNumberEditText.text.toString(),
            country = binding.countryAutoCompleteTextView.text.toString(),
            email = binding.emailEditText.text.toString(),
            password = binding.passwordEditText.text.toString()
        )

        try {
            CoroutineScope(Dispatchers.IO).launch {
                viewModel.insertUser(user)
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@Register, "Registered Successfully", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this@Register, Home::class.java)
                    intent.putExtra("name", user.name)
                    startActivity(intent)
                }
            }
        } catch (e: Exception) {
            Toast.makeText(this@Register, e.localizedMessage!!.toString(), Toast.LENGTH_SHORT).show()
        }
    }
}
