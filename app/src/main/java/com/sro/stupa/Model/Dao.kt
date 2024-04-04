package com.sro.stupa.Model

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface UserDao {
    @Insert
    fun insertUser(user: User)

    @Query("SELECT * FROM users WHERE email=:email AND password = :password")
    fun getUserByEmailAndPassword(email: String, password: String): User

    @Query("SELECT * FROM users")
    fun getAllUsers(): LiveData<List<User>>



}