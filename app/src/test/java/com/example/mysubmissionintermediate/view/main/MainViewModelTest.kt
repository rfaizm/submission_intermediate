package com.example.mysubmissionintermediate.view.main

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.AsyncPagingDataDiffer
import androidx.paging.PagingData
import androidx.paging.PagingSource
import androidx.paging.PagingState
import androidx.recyclerview.widget.ListUpdateCallback
import com.example.mysubmissionintermediate.DataDummy
import com.example.mysubmissionintermediate.MainDispatcherRule
import com.example.mysubmissionintermediate.data.UserRepository
import com.example.mysubmissionintermediate.data.local.entity.StoryLocal
import com.example.mysubmissionintermediate.getOrAwaitValue
import com.example.mysubmissionintermediate.view.adapter.StoryAdapter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class MainViewModelTest{
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()
    @get:Rule
    val mainDispatcherRules = MainDispatcherRule()
    @Mock
    private lateinit var userRepository: UserRepository


    @Test
    fun `when Get Quote Should Not Null and Return Data`() = runTest {
        val dummyQuote = DataDummy.generateDummyStoryResponse()
        val data: PagingData<StoryLocal> = StoryPagingSource.snapshot(dummyQuote)
        val expectedQuote = MutableLiveData<PagingData<StoryLocal>>()
        expectedQuote.value = data
        Mockito.`when`(userRepository.getQuote()).thenReturn(expectedQuote)

        val mainViewModel = MainViewModel(userRepository)
        val actualQuote: PagingData<StoryLocal> = mainViewModel.quote.getOrAwaitValue()

        val differ = AsyncPagingDataDiffer(
            diffCallback = StoryAdapter.DIFF_CALLBACK,
            updateCallback = noopListUpdateCallback,
            workerDispatcher = Dispatchers.Main,
        )
        differ.submitData(actualQuote)

        Assert.assertNotNull(differ.snapshot())
        Assert.assertEquals(dummyQuote.size, differ.snapshot().size)
        Assert.assertEquals(dummyQuote[0], differ.snapshot()[0])
    }

    @Test
    fun `when Get Quote Empty Should Return No Data`() = runTest {
        val data: PagingData<StoryLocal> = PagingData.from(emptyList())
        val expectedQuote = MutableLiveData<PagingData<StoryLocal>>()
        expectedQuote.value = data
        Mockito.`when`(userRepository.getQuote()).thenReturn(expectedQuote)

        val mainViewModel = MainViewModel(userRepository)
        val actualQuote: PagingData<StoryLocal> = mainViewModel.quote.getOrAwaitValue()

        val differ = AsyncPagingDataDiffer(
            diffCallback = StoryAdapter.DIFF_CALLBACK,
            updateCallback = noopListUpdateCallback,
            workerDispatcher = Dispatchers.Main,
        )
        differ.submitData(actualQuote)

        Assert.assertEquals(0, differ.snapshot().size)
    }

}

class StoryPagingSource : PagingSource<Int, LiveData<List<StoryLocal>>>() {
    companion object {
        fun snapshot(items: List<StoryLocal>): PagingData<StoryLocal> {
            return PagingData.from(items)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, LiveData<List<StoryLocal>>>): Int {
        return 0
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, LiveData<List<StoryLocal>>> {
        return LoadResult.Page(emptyList(), 0, 1)
    }
}

val noopListUpdateCallback = object : ListUpdateCallback {
    override fun onInserted(position: Int, count: Int) {}
    override fun onRemoved(position: Int, count: Int) {}
    override fun onMoved(fromPosition: Int, toPosition: Int) {}
    override fun onChanged(position: Int, count: Int, payload: Any?) {}
}


