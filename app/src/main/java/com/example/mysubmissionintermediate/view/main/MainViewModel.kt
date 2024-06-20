package com.example.mysubmissionintermediate.view.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.mysubmissionintermediate.data.UserRepository
import com.example.mysubmissionintermediate.data.api.ListStoryItem
import com.example.mysubmissionintermediate.data.local.entity.StoryLocal
import com.example.mysubmissionintermediate.data.pref.UserModel
import kotlinx.coroutines.launch

class MainViewModel(private val repository: UserRepository) : ViewModel() {
    val quote: LiveData<PagingData<StoryLocal>> =
        repository.getQuote()!!.cachedIn(viewModelScope)

    fun getSession(): LiveData<UserModel> {
        return repository.getSession().asLiveData()
    }

    fun logout() {
        viewModelScope.launch {
            repository.logout()
        }
    }

}