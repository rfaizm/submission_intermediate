package com.example.mysubmissionintermediate.view.login

import androidx.lifecycle.ViewModel
import com.example.mysubmissionintermediate.data.UserRepository

class LoginViewModel(private val repository : UserRepository) : ViewModel()  {
    fun login(email : String, password : String) = repository.login(email, password)
}
