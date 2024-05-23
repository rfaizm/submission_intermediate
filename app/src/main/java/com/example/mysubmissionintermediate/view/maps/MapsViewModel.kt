package com.example.mysubmissionintermediate.view.maps

import androidx.lifecycle.ViewModel
import com.example.mysubmissionintermediate.data.UserRepository

class MapsViewModel(private val repository: UserRepository) : ViewModel()  {

    fun getStoryLocation() = repository.getListStoryLocation()
}