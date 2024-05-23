package com.example.mysubmissionintermediate.data.local.database

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.example.mysubmissionintermediate.data.api.ApiService
import com.example.mysubmissionintermediate.data.local.entity.StoryLocal

@OptIn(ExperimentalPagingApi::class)
class StoryRemoteMediator(
    private val database: StoryDatabase,
    private val apiService: ApiService,
    private val token : String
) : RemoteMediator<Int, StoryLocal>() {

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, StoryLocal>
    ): MediatorResult {
        val page = INITIAL_PAGE_INDEX
        try {
            val responseData = apiService.getStories("Bearer $token",page, state.config.pageSize)
            val endOfPaginationReached = responseData.listStory.isEmpty()
            val mutableListStory: MutableList<StoryLocal> = mutableListOf()
            database.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    database.storyDao().deleteAll()
                }
                responseData.listStory.forEach {
                    val story = StoryLocal(it.id!!, it.name!!, it.description!!, it.createdAt!!, it.photoUrl!!, it.lon, it.lat)
                    mutableListStory.add(story)
                }
                val listStory: List<StoryLocal> = mutableListStory.toList()
                database.storyDao().insertStory(listStory)
            }
            return MediatorResult.Success(endOfPaginationReached = endOfPaginationReached)
        } catch (exception: Exception) {
            return MediatorResult.Error(exception)
        }
    }

    override suspend fun initialize(): InitializeAction {
        return InitializeAction.LAUNCH_INITIAL_REFRESH
    }

    private companion object {
        const val INITIAL_PAGE_INDEX = 1
    }

}