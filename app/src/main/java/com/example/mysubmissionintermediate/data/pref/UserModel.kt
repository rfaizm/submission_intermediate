package com.example.mysubmissionintermediate.data.pref

data class UserModel(
    val userId: String,
    val name : String,
    val token: String,
    val isLogin: Boolean = false
)