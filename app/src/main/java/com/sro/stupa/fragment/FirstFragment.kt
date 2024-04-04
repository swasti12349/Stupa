package com.sro.stupa.fragment

import MainRepository
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.room.Room
import com.sro.stupa.Model.AppDatabase
import com.sro.stupa.Model.UserDao
import com.sro.stupa.Adapter.UserAdapter
import com.sro.stupa.databinding.FragmentFirstBinding
import com.sro.stupa.viewModel.UserViewModel
import com.sro.stupa.viewModel.UserViewModelFactory

class FirstFragment : Fragment() {

    private lateinit var binding: FragmentFirstBinding
    private lateinit var userViewModel: UserViewModel
    private lateinit var userAdapter: UserAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFirstBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val userDao: UserDao =
            Room.databaseBuilder(requireContext(), AppDatabase::class.java, "my_database")
                .build()
                .userDao()

        val mainRepository = MainRepository(userDao)

        userViewModel = ViewModelProvider(this, UserViewModelFactory(mainRepository))
            .get(UserViewModel::class.java)

        binding.userRV.layoutManager = LinearLayoutManager(requireContext())

        // Observe the LiveData returned by the Room database query
        userViewModel.getAllUsers().observe(viewLifecycleOwner, Observer { userList ->
            userAdapter = UserAdapter(userList)
            binding.userRV.adapter = userAdapter
        })

    }
}
