package com.example.mysubmissionintermediate.view.detail

import androidx.lifecycle.ViewModel
import com.example.mysubmissionintermediate.data.UserRepository

class DetailStoryViewModel(private val repository: UserRepository) : ViewModel() {

    fun getDetailStory(id : String) = repository.getDetailStory(id)
}