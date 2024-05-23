package com.example.mysubmissionintermediate.data

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.mysubmissionintermediate.data.api.ApiService
import com.example.mysubmissionintermediate.data.api.ListStoryItem
import com.example.mysubmissionintermediate.data.api.StoryResponse

class StoryPagingSource(private val apiService: ApiService, private  val tokenUser : String) : PagingSource<Int, ListStoryItem>() {
    override fun getRefreshKey(state: PagingState<Int, ListStoryItem>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ListStoryItem> {
        return try {
            val position = params.key ?: INITIAL_PAGE_INDEX

            val responseData = apiService.getStories(page = position, size = params.loadSize, token = "Bearer $tokenUser")
            val dataStory = responseData.listStory
            Log.d("StoryPagingSource", "Response data: $dataStory")
            LoadResult.Page(
                data = dataStory,
                prevKey = if (position == INITIAL_PAGE_INDEX) null else position - 1,
                nextKey = if (dataStory.isNullOrEmpty()) null else position + 1
            )
        } catch (exception: Exception) {
            return LoadResult.Error(exception)
        }
    }


    private companion object {
        const val INITIAL_PAGE_INDEX = 1
    }

}