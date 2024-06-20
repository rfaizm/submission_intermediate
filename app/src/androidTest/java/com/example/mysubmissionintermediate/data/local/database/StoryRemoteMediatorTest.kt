package com.example.mysubmissionintermediate.data.local.database

import android.content.Context
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.preferencesDataStoreFile
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingConfig
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.mysubmissionintermediate.data.api.ApiService
import com.example.mysubmissionintermediate.data.api.DetailStoryResponse
import com.example.mysubmissionintermediate.data.api.ListStoryItem
import com.example.mysubmissionintermediate.data.api.LoginResponse
import com.example.mysubmissionintermediate.data.api.RegisterResponse
import com.example.mysubmissionintermediate.data.api.StoryResponse
import com.example.mysubmissionintermediate.data.local.entity.StoryLocal
import com.example.mysubmissionintermediate.data.pref.UserPreference
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import kotlin.coroutines.coroutineContext

//@OptIn(ExperimentalCoroutinesApi::class)
//@ExperimentalPagingApi
//@RunWith(AndroidJUnit4::class)
//class StoryRemoteMediatorTest {
//    private var mockApi: ApiService = FakeApiService()
//    private var mockDb: StoryDatabase = Room.inMemoryDatabaseBuilder(
//        ApplicationProvider.getApplicationContext(),
//        StoryDatabase::class.java
//    ).allowMainThreadQueries().build()
//    private lateinit var preference: UserPreference
//
//
//    @Before
//    fun setUp() {
//        val context = ApplicationProvider.getApplicationContext<Context>()
//        val dataStore = PreferenceDataStoreFactory.create {
//            context.preferencesDataStoreFile("test_preferences")
//        }
//        preference = UserPreference.getInstance(dataStore)
//    }
//
//    @Test
//    fun refreshLoadReturnsSuccessResultWhenMoreDataIsPresent() = runTest {
//        val remoteMediator = StoryRemoteMediator(
//            mockDb,
//            mockApi,
//            preference.getAuthToken1()!!
//        )
//        val pagingState = PagingState<Int, StoryLocal>(
//            listOf(),
//            null,
//            PagingConfig(10),
//            10
//        )
//        val result = remoteMediator.load(LoadType.REFRESH, pagingState)
//        assertTrue(result is RemoteMediator.MediatorResult.Success)
//        assertFalse((result as RemoteMediator.MediatorResult.Success).endOfPaginationReached)
//    }
//    @After
//    fun tearDown() {
//        mockDb.clearAllTables()
//    }
//}
//
//class FakeApiService : ApiService {
//    override suspend fun register(name: String, email: String, password: String): RegisterResponse {
//        TODO("Not yet implemented")
//    }
//
//    override suspend fun login(email: String, password: String): LoginResponse {
//        TODO("Not yet implemented")
//    }
//
//    override suspend fun getStories(token: String, page: Int, size: Int): StoryResponse {
//        val items: MutableList<ListStoryItem> = arrayListOf()
//
//        for (i in 0..100) {
//            val storyItem = ListStoryItem(
//                photoUrl = "photo $i",
//                createdAt = "created at $i",
//                name = "author $i",
//                description = "quote $i",
//                id = i.toString(),
//                lon = i.toDouble(),
//                lat = i.toDouble()
//            )
//            items.add(storyItem)
//        }
//
//        val paginatedItems = items.subList((page - 1) * size, minOf(page * size, items.size))
//        return StoryResponse(listStory = paginatedItems)
//    }
//
//    override suspend fun getDetailStories(userId: String, token: String): DetailStoryResponse {
//        TODO("Not yet implemented")
//    }
//
//    override suspend fun uploadImage(
//        token: String,
//        file: MultipartBody.Part,
//        description: RequestBody
//    ): RegisterResponse {
//        TODO("Not yet implemented")
//    }
//
//    override suspend fun getStoriesLocation(token: String, location: Int): StoryResponse {
//        TODO("Not yet implemented")
//    }
//}