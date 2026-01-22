package com.example.myapp.model

data class User(
    val username: String,
    val email: String = "",
    val phone: String = ""
)