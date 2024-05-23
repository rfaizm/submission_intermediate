package com.example.mysubmissionintermediate.data.di

import android.content.Context
import com.example.mysubmissionintermediate.data.UserRepository
import com.example.mysubmissionintermediate.data.api.ApiConfig
import com.example.mysubmissionintermediate.data.local.database.StoryDatabase
import com.example.mysubmissionintermediate.data.pref.UserPreference
import com.example.mysubmissionintermediate.data.pref.dataStore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

object Injection {
    fun provideRepository(context: Context): UserRepository {
        val pref = UserPreference.getInstance(context.dataStore)
        val apiService = ApiConfig.getApiService()
        val database = StoryDatabase.getDatabase(context)
        return UserRepository.getInstance(apiService, pref, database)
    }
}