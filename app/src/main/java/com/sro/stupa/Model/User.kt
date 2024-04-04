package com.sro.stupa.Model

import androidx.room.Entity

import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class User(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val phoneNumber: String,
    val country: String,
    val email: String,
    val password: String
)
