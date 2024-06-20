package com.example.mysubmissionintermediate.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.example.mysubmissionintermediate.data.api.ApiService
import com.example.mysubmissionintermediate.data.api.LoginResponse
import com.example.mysubmissionintermediate.data.api.RegisterResponse
import com.example.mysubmissionintermediate.data.local.database.StoryDatabase
import com.example.mysubmissionintermediate.data.local.database.StoryRemoteMediator
import com.example.mysubmissionintermediate.data.local.entity.StoryLocal
import com.example.mysubmissionintermediate.data.pref.UserModel
import com.example.mysubmissionintermediate.data.pref.UserPreference
import com.google.gson.Gson
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.HttpException

class UserRepository private constructor(
    private val apiService: ApiService, private val userPreference: UserPreference, private val database: StoryDatabase
)  {

    fun register(name : String, email : String, password : String) = liveData {
        emit(ResultState.Loading)

        try {
            val successResponse = apiService.register(name, email, password)
            emit(ResultState.Success(successResponse))
        } catch (e : HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            val errorResponse = Gson().fromJson(errorBody, RegisterResponse::class.java)
            emit(ResultState.Error(errorResponse.message))
        }
    }

    fun login(email : String, password: String) = liveData {
        emit(ResultState.Loading)

        try {
            val successResponse = apiService.login(email, password)
            val userIdBody = successResponse.loginResult?.userId.toString()
            val nameBody = successResponse.loginResult?.name.toString()
            val tokenBody = successResponse.loginResult?.token.toString()
            saveSession(UserModel(userIdBody, nameBody, tokenBody))
            emit(ResultState.Success(successResponse))
        } catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            val errorResponse = Gson().fromJson(errorBody, LoginResponse::class.java)
            emit(errorResponse.message?.let { ResultState.Error(it) })
        }
    }


    fun getQuote(): LiveData<PagingData<StoryLocal>>? {
        return if (userPreference.isUserLoggedIn()) {
            val token = userPreference.getAuthToken1()
            @OptIn(ExperimentalPagingApi::class)
            Pager(
                config = PagingConfig(
                    pageSize = 5
                ),
                remoteMediator = StoryRemoteMediator(database = database, apiService =  apiService, token =  token!!),
                pagingSourceFactory = {
                    // QuotePagingSource(apiService)
                    database.storyDao().getStories()
                }
            ).liveData
        } else {
            MutableLiveData<PagingData<StoryLocal>>().apply { value = null }
        }
    }



    fun getDetailStory(id : String) = liveData {
        emit(ResultState.Loading)

        try {
            val tokenFlow: Flow<String?> = userPreference.getAuthToken()

            // Collect the token value from the flow
            val token = tokenFlow.firstOrNull() ?: ""

            // Check if token is empty
            if (token.isEmpty()) {
                emit(ResultState.Error("Token is empty"))
                return@liveData
            }

            val successResponse = apiService.getDetailStories(userId = id, token = "Bearer $token")

            emit(ResultState.Success(successResponse))
        } catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            val errorResponse = Gson().fromJson(errorBody, LoginResponse::class.java)
            emit(ResultState.Error(errorResponse.message ?: "Unknown error"))
        } catch (e: Exception) {
            emit(ResultState.Error(e.message ?: "Unknown error"))
        }
    }

    fun uploadImage(photo : MultipartBody.Part, desc : RequestBody) = liveData {
        try {
            val tokenFlow: Flow<String?> = userPreference.getAuthToken()

            // Collect the token value from the flow
            val token = tokenFlow.firstOrNull() ?: ""

            // Check if token is empty
            if (token.isEmpty()) {
                emit(ResultState.Error("Token is empty"))
                return@liveData
            }

            val successResponse = apiService.uploadImage(token = "Bearer $token", file = photo, description = desc)
            emit(ResultState.Success(successResponse))
        } catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            val errorResponse = Gson().fromJson(errorBody, LoginResponse::class.java)
            emit(ResultState.Error(errorResponse.message ?: "Unknown error"))
        } catch (e: Exception) {
            emit(ResultState.Error(e.message ?: "Unknown error"))
        }
    }

    fun getListStoryLocation() = liveData {
        emit(ResultState.Loading)

        try {
            val tokenFlow: Flow<String?> = userPreference.getAuthToken()

            // Collect the token value from the flow
            val token = tokenFlow.firstOrNull() ?: ""

            // Check if token is empty
            if (token.isEmpty()) {
                emit(ResultState.Error("Token is empty"))
                return@liveData
            }

            val successResponse = apiService.getStoriesLocation(token = "Bearer $token")

            emit(ResultState.Success(successResponse))
        } catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            val errorResponse = Gson().fromJson(errorBody, LoginResponse::class.java)
            emit(ResultState.Error(errorResponse.message ?: "Unknown error"))
        } catch (e: Exception) {
            emit(ResultState.Error(e.message ?: "Unknown error"))
        }
    }

    private suspend fun saveSession(user: UserModel) {
        userPreference.saveSession(user)
    }

    fun getSession(): Flow<UserModel> {
        return userPreference.getSession()
    }

    suspend fun logout() {
        userPreference.logout()
    }

    companion object {
        @Volatile
        private var instance: UserRepository? = null
        fun getInstance(apiService: ApiService, userPreference: UserPreference, database: StoryDatabase) =
            instance ?: synchronized(this) {
                instance ?: UserRepository(apiService, userPreference, database)
            }.also { instance = it }
    }
}