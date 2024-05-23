package com.example.mysubmissionintermediate.view.image

import androidx.lifecycle.ViewModel
import com.example.mysubmissionintermediate.data.UserRepository
import okhttp3.MultipartBody
import okhttp3.RequestBody

class UploadViewModel(private val repository: UserRepository) : ViewModel()  {
    fun uploadImage(photo : MultipartBody.Part, desc : RequestBody) = repository.uploadImage(photo, desc)
}